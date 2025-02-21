package br.com.alfatek.indikey.data

import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.model.User
import br.com.alfatek.indikey.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth:FirebaseAuth) : AuthRepository {

    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun signIn(email: String, password: String): Resource<FirebaseUser> {
       return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
           Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
           Resource.Error(e)
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override fun getUsers(): Flow<Resource<List<User>>> {

        return flow {
            emit(Resource.Loading)
            try {
                val snapshot = firebaseFirestore.collection("users").get().await()
                val users = snapshot.toObjects(User::class.java)
                emit(Resource.Success(users))
                } catch (e: Exception) {
                emit(Resource.Error(e))
            }

        }
    }

    override suspend fun<T : Any> saveToFirestore(collection:String, data: T) {
        try {
            val result = firebaseFirestore.collection(collection).add(data).await()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override suspend fun getClient(cnpj: String): Resource<Cliente> {
        return try {
            val snapshot = firebaseFirestore.collection("clientes").document(cnpj).get().await()
            val cliente = snapshot.toObject(Cliente::class.java)
            Resource.Success(cliente!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }

    override suspend fun getClients(): Resource<List<Cliente>> {
        return try {

            val snapshot = firebaseFirestore.collection("clientes").get().await()
            val clientes: List<Cliente> = snapshot.toObjects(Cliente::class.java)
            Resource.Success(clientes)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
    override suspend fun getClientByCnpj(cnpj: String,Cliente: Class<*>): Result<Any> {
        return try {
            val querySnapshot = firebaseFirestore.collection("clientes")
                .whereEqualTo("cnpj", cnpj)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Result.failure(Exception("Client not found with CNPJ: $cnpj"))
            } else {
                val client = querySnapshot.documents[0].toObject(Cliente)
                if (client != null) {
                    Result.success(client)
                } else {
                    Result.failure(Exception("Error converting document to Client object"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateClient(cliente: Cliente) {
        try {
            firebaseFirestore.collection("clientes").document(cliente.cnpj).set(cliente).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }


    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
}