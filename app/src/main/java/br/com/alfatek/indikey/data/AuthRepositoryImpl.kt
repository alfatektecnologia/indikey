package br.com.alfatek.indikey.data

import android.util.Log
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
    private var documentId: String = ""

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

    override suspend fun getClients(): List<Cliente>? {
        return try {
            val snapshot = firebaseFirestore.collection("clientes").get().await()
            snapshot.toObjects(Cliente::class.java)

            //Resource.Success(clientes)
        } catch (e: Exception) {
            println("Error getting documents: $e")
            null
        }
    }

    override suspend fun getActiveClients(): List<Cliente>? {

       return try {
           val snapshot = firebaseFirestore.collection("clientes")
               .whereEqualTo("isActive",  true)
               .get()
               .await()
           if (snapshot.isEmpty){
                emptyList()
           }else{
              snapshot.toObjects(Cliente::class.java)
           }

       }catch (e: Exception) {
           println("Error getting documents: $e")
           null
       }
    }

    override suspend fun getPendingClients(): List<Cliente>? {
        return try {
            val snapshot = firebaseFirestore.collection("clientes")
                .whereEqualTo("isPending",  true)
                .get()
                .await()
            if (snapshot.isEmpty){
                emptyList()
            }else{
                snapshot.toObjects(Cliente::class.java)
            }

        }catch (e: Exception) {
            println("Error getting documents: $e")
            null
        }
    }


    override suspend fun getClientByCnpj(cnpj: String,Cliente: Class<*>): Result<Cliente> {
        return try {
            val querySnapshot = firebaseFirestore.collection("clientes")
                .whereEqualTo("cnpj", cnpj)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Result.failure(Exception("Client not found with CNPJ: $cnpj"))
            } else {
                documentId = querySnapshot.documents[0].id
                Log.d("AuthRepositoryImpl", "getClientByCnpj: $documentId")
                val client = querySnapshot.documents[0].toObject(Cliente)
                if (client != null) {
                    Result.success(client) as Result<Cliente>
                } else {
                    Result.failure(Exception("Error converting document to Client object"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override val documentID: String
        get() = documentId

    override fun getDocumentId(): String {
        return documentId
    }

    override suspend fun updateClient(cliente: Cliente,documentId: String) {
        try {
            firebaseFirestore.collection("clientes").document(documentId).set(cliente).await()
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