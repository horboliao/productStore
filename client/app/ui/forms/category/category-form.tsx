'use client'
import React, {useState} from 'react';
import {Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button, useDisclosure, Checkbox, Input, Link} from "@nextui-org/react";
import * as z from "zod";
import {Controller, useForm} from "react-hook-form";
import {Category} from "@/lib/types";
import {Textarea} from "@nextui-org/input";
import {zodResolver} from "@hookform/resolvers/zod";
import {useRouter} from "next/navigation";
import {createCategory, updateCategory} from "@/actions/categories";

const formSchema = z.object({
    name: z.string().min(1, {
        message: "Name is required",
    }),
    description: z.string().min(1, {
        message: "Description is required",
    }),
});

interface NewCategoryForm {
    category?: Category;
    isOpen: boolean;
    onOpenChange: () => void;
}
const CategoryForm = ({category, isOpen, onOpenChange}:NewCategoryForm) => {
    const router = useRouter();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: category?.name,
            description: category?.description
        },
    });

    const { handleSubmit, control } = form;
    const { isSubmitting, isValid } = form.formState;

    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        if (!category) {
            await createCategory(values)
            router.refresh();
        } else {
            await updateCategory(category.id, values)
            router.refresh();
        }
    }
    return (
        <>
            <Modal
                isOpen={isOpen}
                onOpenChange={onOpenChange}
                placement="top-center"
            >
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">Category</ModalHeader>
                            <ModalBody>
                                <form
                                    onSubmit={handleSubmit(onSubmit)}
                                    className="space-y-4 mt-4"
                                >
                                    <Controller
                                        name="name"
                                        control={control}
                                        render={({ field, fieldState }) => (
                                            <Input
                                                {...field}
                                                isRequired
                                                disabled={isSubmitting}
                                                label={'Name'}
                                                isInvalid={fieldState.invalid}
                                                errorMessage={fieldState.error?.message}
                                            />
                                        )}
                                    />
                                    <Controller
                                        name="description"
                                        control={control}
                                        render={({ field, fieldState }) => (
                                            <Textarea
                                                {...field}
                                                isRequired
                                                label={'Description'}
                                                isInvalid={fieldState.invalid}
                                                errorMessage={fieldState.error?.message}
                                            />
                                        )}
                                    />
                                    <div className="flex items-center gap-x-2">
                                        <Button
                                            type="submit"
                                            color='primary'
                                            className={'w-full'}
                                            disabled={!isValid || isSubmitting}
                                        >
                                            Submit
                                        </Button>
                                    </div>
                                </form>
                            </ModalBody>
                            <ModalFooter>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    );
};

export default CategoryForm;