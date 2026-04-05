package com.example.unikart.data.repository

import com.example.unikart.data.model.Item
import com.example.unikart.domain.repository.ItemRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ItemRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ItemRepository {

    override suspend fun addItem(item: Item): Result<Unit> {
        return try {
            firestore.collection("items")
                .add(item)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getItems(): Flow<List<Item>> = callbackFlow {

        val listener = firestore.collection("items")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Item::class.java)?.copy(
                        id = doc.id,
                        isSold = doc.getBoolean("isSold") ?: false
                        )
                } ?: emptyList()

                trySend(items).getOrNull()
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun getItemById(id: String): Item? {
        return try {
            val doc = firestore.collection("items")
                .document(id)
                .get()
                .await()

            doc.toObject(Item::class.java)?.copy(
                id = doc.id,
                isSold = doc.getBoolean("isSold") ?: false
                )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addToFavorites(userId: String, itemId: String) {
        val docId = "${userId}_$itemId"
        firestore.collection("favorites")
            .document(docId)
            .set(
                mapOf(
                    "userId" to userId,
                    "itemId" to itemId
                )
            ).await()
    }


    override suspend fun removeFromFavorites(userId: String, itemId: String) {
        val docId = "${userId}_${itemId}"

        firestore.collection("favorites")
            .document(docId)
            .delete()
            .await()
    }

    override fun getFavorites(userId: String): Flow<List<String>> = callbackFlow {

        val listener = firestore.collection("favorites")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->

                val favIds = snapshot?.documents?.mapNotNull {
                    it.getString("itemId")
                } ?: emptyList()

                trySend(favIds)
            }

        awaitClose { listener.remove() }
    }

    override fun getItemsByUser(userId: String): Flow<List<Item>> = callbackFlow {

        val listener = firestore.collection("items")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull {
                    it.toObject(Item::class.java)?.copy(id = it.id,
                        isSold = it.getBoolean("isSold") ?: false
                        )
                } ?: emptyList()

                trySend(items)
            }

        awaitClose { listener.remove() }
    }

override suspend fun updateItem(item: Item) {
    firestore.collection("items")
        .document(item.id)
        .set(item)
        .await()
}


    override suspend fun deleteItem(itemId: String) {
        firestore.collection("items")
            .document(itemId)
            .delete()
    }

}