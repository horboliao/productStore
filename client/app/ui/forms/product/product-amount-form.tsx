'use client'
import React from 'react';
import * as z from "zod";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Button, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader} from "@nextui-org/react";
import { ProductWithCategory} from "@/lib/types";

const formSchema = z.object({
    amount: z.coerce.number().positive(),
});

interface ProductAmountFormProps {
    product: ProductWithCategory;
    increaseAmount?: boolean;
    isOpen: boolean;
    onOpenChange: () => void;
}
const ProductAmountForm = ({product, increaseAmount, isOpen, onOpenChange}:ProductAmountFormProps) => {
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            amount: 1
        },
    });

    const { handleSubmit, control } = form;
    const { isSubmitting, isValid } = form.formState;
    const amount = form.watch('amount');
    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        console.log(values)
    }
    return (
        <Modal
            isOpen={isOpen}
            onOpenChange={onOpenChange}
            placement="top-center"
        >
            <ModalContent>
                {(onClose) => (
                    <>
                        <ModalHeader className="flex flex-col gap-1">{increaseAmount ? 'Increase' : 'Decrease'} {product.name} amount</ModalHeader>
                        <ModalBody>
                            <form
                                onSubmit={handleSubmit(onSubmit)}
                                className="space-y-4 mt-4"
                            >
                                <Controller
                                    name="amount"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <Input
                                            {...field}
                                            isRequired
                                            type='number'
                                            disabled={isSubmitting}
                                            label={'Amount'}
                                            isInvalid={fieldState.invalid}
                                            errorMessage={fieldState.error?.message}
                                        />
                                    )}
                                />
                                <div className="flex gap-1">
                                    <p className="font-semibold text-default-400 text-small">{product.amount}</p>
                                    <p className="text-default-400 text-small">in store</p>
                                </div>
                                <div className="flex items-center gap-x-2">
                                    <Button
                                        type="submit"
                                        color={!increaseAmount && amount > product.amount ? 'default' : 'primary'}
                                        className={'w-full'}
                                        disabled={!isValid || isSubmitting || (!increaseAmount && amount > product.amount)}
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
    );
};

export default ProductAmountForm;