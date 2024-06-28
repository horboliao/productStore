'use client'
import React, {useEffect, useState} from 'react';
import ProductCard from "@/app/ui/cards/product-card";
import {Category, ProductWithCategory} from "@/lib/types";
import {Input, Select, SelectItem} from "@nextui-org/react";
import {useSession} from "next-auth/react";

interface ProductListProps {
    products: ProductWithCategory[];
    categories: Category[];
}
const ProductList = ({products, categories}:ProductListProps) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('');

    const handleCategoryChange = (value: string) => {
        setSelectedCategory(value);
    };

    const filteredProducts = products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesCategory = selectedCategory ? product.category === selectedCategory : true;
        return matchesSearch && matchesCategory;
    });

    if (!categories ) {
        return <p>No products found.</p>;
    }

    return (
        <div>
            <div className="flex space-x-4 my-4">
                <Input
                    label="Search"
                    placeholder="Search products..."
                    value={searchTerm}
                    onValueChange={setSearchTerm}
                />
                <Select
                    label="Category"
                    placeholder="Select a category"
                    selectedKeys={[selectedCategory]}
                    onSelectionChange={(keys) => handleCategoryChange(Array.from(keys).join(""))}
                >
                    <SelectItem key="">All Categories</SelectItem>
                    {categories.map(category => (
                        <SelectItem key={category.name}>{category.name}</SelectItem>
                    ))}
                </Select>
            </div>
            <div className='grid grid-cols-4 gap-2 pt-6'>
                {filteredProducts.map(product => (
                    <ProductCard product={product} key={product.id} categories={categories} />
                ))}
            </div>
        </div>
    );
};

export default ProductList;