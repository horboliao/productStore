export interface NewCategory  {
    name: string;
    description: string;
}

export interface Category extends NewCategory{
    id: number|string;
}

export interface CategoryWithProductsCount extends Category{
    amount: number;
}

export interface NewProduct {
    category: number;
    name: string;
    description: string;
    producer: string;
    amount: number;
    price: number;
}

export interface Product extends NewProduct{
    id: number|string;
}

export interface ProductWithCategory{
    id: number;
    category: string;
    name: string;
    description: string;
    producer: string;
    amount: number;
    price: number;
}