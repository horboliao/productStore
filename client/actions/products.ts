import {Category, Product, ProductWithCategory} from "@/lib/types";
import {getCategories} from "@/actions/categories";

export async function getProducts (): Promise<ProductWithCategory[]> {
    const categories: Category[] = await getCategories();
    const products: Product[] = [
        {
            id: 1,
            category: 1, // Electronics
            name: "Smartphone X",
            description: "A high-end smartphone with a sleek design and powerful features.",
            producer: "TechCorp",
            amount: 50,
            price: 999.99
        },
        {
            id: 2,
            category: 2, // Fashion
            name: "Leather Jacket",
            description: "A stylish leather jacket perfect for casual and formal wear.",
            producer: "Fashionista",
            amount: 100,
            price: 199.99
        },
        {
            id: 3,
            category: 3, // Home and Kitchen
            name: "Blender Pro",
            description: "A high-speed blender suitable for making smoothies and soups.",
            producer: "KitchenMaster",
            amount: 75,
            price: 89.99
        },
        {
            id: 4,
            category: 4, // Beauty and Personal Care
            name: "Moisturizing Cream",
            description: "A hydrating cream that keeps your skin soft and smooth.",
            producer: "SkinCare Inc.",
            amount: 200,
            price: 24.99
        },
        {
            id: 5,
            category: 5, // Sports and Outdoors
            name: "Mountain Bike",
            description: "A durable mountain bike designed for rough terrains.",
            producer: "BikePro",
            amount: 30,
            price: 499.99
        },
        {
            id: 6,
            category: 6, // Toys and Games
            name: "Action Figure Set",
            description: "A set of popular action figures for kids of all ages.",
            producer: "ToyWorld",
            amount: 150,
            price: 39.99
        },
        {
            id: 7,
            category: 7, // Books and Stationery
            name: "Classic Novel",
            description: "A timeless classic novel that belongs in every book collection.",
            producer: "LiteraryWorks",
            amount: 80,
            price: 14.99
        },
        {
            id: 8,
            category: 8, // Automotive
            name: "Car Wax",
            description: "A premium car wax that provides a long-lasting shine.",
            producer: "AutoCare",
            amount: 120,
            price: 15.99
        },
        {
            id: 9,
            category: 9, // Health and Wellness
            name: "Fitness Tracker",
            description: "A wearable fitness tracker that monitors your health and activity levels.",
            producer: "FitTech",
            amount: 60,
            price: 129.99
        },
        {
            id: 10,
            category: 10, // Groceries and Gourmet Food
            name: "Organic Honey",
            description: "A jar of pure organic honey sourced from local farms.",
            producer: "Nature's Best",
            amount: 180,
            price: 12.99
        }
    ];

    return products.map(product => ({
        ...product,
        category: categories.find(category => category.id === product.category)!
    }));

}