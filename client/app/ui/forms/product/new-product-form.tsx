'use client'
import React from 'react';
import {Button, useDisclosure} from "@nextui-org/react";
import ProductForm from "@/app/ui/forms/product/product-form";
import {Category} from "@/lib/types";

interface NewProductFormProps {
    categories: Category[];
}
const NewProductForm = ({categories}:NewProductFormProps) => {
    const {isOpen, onOpen, onOpenChange} = useDisclosure();
    return (
        <>
            <Button onPress={onOpen} color="primary">New product</Button>
            <ProductForm categories={categories} isOpen={isOpen} onOpenChange={onOpenChange}/>
        </>
    );
};

export default NewProductForm;