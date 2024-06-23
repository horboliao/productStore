'use client'
import React, {useState} from 'react';
import {Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button, useDisclosure, Checkbox, Input, Link} from "@nextui-org/react";
import * as z from "zod";
import {Controller, useForm} from "react-hook-form";
import {Textarea} from "@nextui-org/input";
import {zodResolver} from "@hookform/resolvers/zod";
import ProductForm from "@/app/ui/forms/product/product-form";
import CategoryForm from "@/app/ui/forms/category/category-form";


const NewCategoryForm = () => {
    const {isOpen, onOpen, onOpenChange} = useDisclosure();
    return (
        <>
            <Button onPress={onOpen} color="primary">New category</Button>
            <CategoryForm isOpen={isOpen} onOpenChange={onOpenChange}/>
        </>
    );
};

export default NewCategoryForm;