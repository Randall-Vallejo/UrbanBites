package com.ucb.app.core.data.db.repository

import com.ucb.app.core.data.db.dao.CartDao
import com.ucb.app.core.data.db.entity.CartItemEntity

class CartRepository(private val cartDao: CartDao) {

    suspend fun addToCart(productId: Int) {
        val item = CartItemEntity(
            productId = productId,
            quantity = 1
        )
        cartDao.insertItem(item)
    }

    suspend fun getCart() = cartDao.getCartItems()
}
