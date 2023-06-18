package com.amrullaev.myolx.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amrullaev.myolx.database.entity.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("select * from Product")
    fun getProduct(): LiveData<List<Product>>

    @Query("select * from Product")
    fun getProductForLike(): List<Product>

    @Delete
     fun deleteProduct(product: Product)

}