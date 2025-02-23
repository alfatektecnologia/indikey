package br.com.alfatek.indikey.data

import br.com.alfatek.indikey.model.Cliente
import br.com.alfatek.indikey.model.User
import br.com.alfatek.indikey.util.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
     //Authentication
     val currentUser: FirebaseUser?
     suspend fun signIn(email: String, password: String): Resource<FirebaseUser>
     suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
     fun signOut()

     //Firestore
     fun getUsers(): Flow<Resource<List<User>>>
     suspend fun<T: Any> saveToFirestore(collection:String, data: T)
     suspend fun getClient(cnpj: String): Resource<Cliente>
     suspend fun getClients(): List<Cliente>?
     suspend fun getActiveClients(): List<Cliente>?
     suspend fun getPendingClients(): List<Cliente>?
     suspend fun updateClient(cliente: Cliente,documentId: String)
     suspend fun getClientByCnpj(cnpj: String,Cliente: Class<*>): Result<Cliente>
     val documentID: String
     fun getDocumentId(): String


}
