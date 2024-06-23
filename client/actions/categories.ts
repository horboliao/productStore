import {Category, CategoryWithProductsCount} from "@/lib/types";
import {getProductByCategory} from "@/actions/products";

export async function getCategories (): Promise<Category[]> {
    const categories : Category[] = [
        {
            id: 1,
            name: "Electronics",
            description: "Devices and gadgets including phones, laptops, and cameras."
        },
        {
            id: 2,
            name: "Fashion",
            description: "Clothing, accessories, and footwear for men, women, and children."
        },
        {
            id: 3,
            name: "Home and Kitchen",
            description: "Furniture, appliances, and kitchenware for home improvement and daily use."
        },
        {
            id: 4,
            name: "Beauty and Personal Care",
            description: "Cosmetics, skincare products, and personal hygiene items."
        },
        {
            id: 5,
            name: "Sports and Outdoors",
            description: "Equipment, clothing, and accessories for various sports and outdoor activities."
        },
        {
            id: 6,
            name: "Toys and Games",
            description: "Toys, board games, and video games for all ages."
        },
        {
            id: 7,
            name: "Books and Stationery",
            description: "Books, notebooks, and office supplies for reading and writing needs."
        },
        {
            id: 8,
            name: "Automotive",
            description: "Car accessories, parts, and tools for vehicle maintenance and enhancement."
        },
        {
            id: 9,
            name: "Health and Wellness",
            description: "Supplements, fitness equipment, and health monitoring devices."
        },
        {
            id: 10,
            name: "Groceries and Gourmet Food",
            description: "Food items, beverages, and gourmet treats for daily consumption and special occasions."
        }
    ];

    return categories;
}

export async function getCategoriesWithProductsCount(): Promise<CategoryWithProductsCount[]> {
    const categories: Category[] = await getCategories();

    const categoriesWithProductCountPromises = categories.map(async (category) => {
        const products = await getProductByCategory(category.id);
        return {
            ...category,
            amount: products.length
        };
    });

    return Promise.all(categoriesWithProductCountPromises);
}