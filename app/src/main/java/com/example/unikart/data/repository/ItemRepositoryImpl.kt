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
                    doc.toObject(Item::class.java)?.copy(id = doc.id)
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

            doc.toObject(Item::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addToFavorites(userId: String, itemId: String) {
        firestore.collection("favorites")
            .add(
                mapOf(
                    "userId" to userId,
                    "itemId" to itemId
                )
            ).await()
    }

    override suspend fun removeFromFavorites(userId: String, itemId: String) {
        val snapshot = firestore.collection("favorites")
            .whereEqualTo("userId", userId)
            .whereEqualTo("itemId", itemId)
            .get()
            .await()

        snapshot.documents.forEach {
            it.reference.delete()
        }
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

}