import Image from "next/image";

import CategoryForm from "@/app/ui/forms/category/new-category-form";
import React from "react";
import {getProducts} from "@/actions/products";
import {Category, ProductWithCategory} from "@/lib/types";
import ProductCard from "@/app/ui/cards/product-card";
import {getCategories} from "@/actions/categories";
import NewProductForm from "@/app/ui/forms/product/new-product-form";

export default async function Home() {
    const products: ProductWithCategory[] = await getProducts();
    const categories: Category[] = await getCategories();
    return (
        <div>
            <div className='flex flex-row justify-between'>
                <h2 className='text-3xl font-semibold'>Products</h2>
                <NewProductForm categories={categories}/>
            </div>
            <div className='grid grid-cols-4 gap-2 pt-6'>
                {
                    products.map((product) => <ProductCard product={product} key={product.id} categories={categories}/>)
                }
            </div>
        </div>
    );
}
