import React from 'react';
import {Category, CategoryWithProductsCount} from "@/lib/types";
import {getCategories, getCategoriesWithProductsCount} from "@/actions/categories";
import CategoryCard from "@/app/ui/cards/category-card";
import CategoryForm from "@/app/ui/forms/category/new-category-form";

const Page = async () => {
    const categories: CategoryWithProductsCount[] = await getCategoriesWithProductsCount();
    return (
        <div>
            <div className='flex flex-row justify-between'>
                <h2 className='text-3xl font-semibold'>Categories</h2>
                <CategoryForm/>
            </div>
            <div className='grid grid-cols-4 gap-2 pt-6'>
                {
                    categories.map((category) => <CategoryCard category={category} key={category.id}/>)
                }
            </div>
        </div>
    );
};

export default Page;