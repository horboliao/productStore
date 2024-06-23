import React from 'react';
import * as z from "zod";
import {Category, ProductWithCategory} from "@/lib/types";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {
    Button,
    Input,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Select,
    SelectItem
} from "@nextui-org/react";
import {Textarea} from "@nextui-org/input";

const formSchema = z.object({
    name: z.string().min(1, {
        message: "Name is required",
    }),
    description: z.string().min(1, {
        message: "Description is required",
    }),
    producer: z.string().min(1, {
        message: "Producer is required",
    }),
    amount: z.coerce.number().positive(),
    category: z.coerce.number(),
    price: z.coerce.number().positive(),
});
interface ProductFormProps {
    product?: ProductWithCategory;
    categories: Category[];
    isOpen: boolean;
    onOpenChange: () => void;
}
const ProductForm = ({product, categories, onOpenChange, isOpen}:ProductFormProps) => {
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: product?.name,
            description: product?.description,
            producer: product?.producer,
            amount: product?.amount,
            price: product?.price,
            category: product?.category.id
        },
    });

    const { handleSubmit, control } = form;
    const { isSubmitting, isValid } = form.formState;
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
                        <ModalHeader className="flex flex-col gap-1">Product form</ModalHeader>
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
                                <Controller
                                    name="producer"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <Input
                                            {...field}
                                            isRequired
                                            disabled={isSubmitting}
                                            label={'Producer'}
                                            isInvalid={fieldState.invalid}
                                            errorMessage={fieldState.error?.message}
                                        />
                                    )}
                                />
                                <Controller
                                    name="category"
                                    control={control}
                                    render={({ field, fieldState }) => (
                                        <Select
                                            label="Category"
                                            isDisabled = {isSubmitting}
                                            errorMessage={fieldState.error?.message}
                                            selectedKeys={[product?.category.id.toString()]}
                                            items={categories}
                                            onChange={(e) => { field.onChange(e); }}
                                        >
                                            {(item) => <SelectItem {...field} key={item.id}>{item.name}</SelectItem>}
                                        </Select>
                                    )}
                                />
                                <div className='grid grid-cols-2 gap-2'>
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
                                    <Controller
                                        name="price"
                                        control={control}
                                        render={({ field, fieldState }) => (
                                            <Input
                                                {...field}
                                                isRequired
                                                type='number'
                                                disabled={isSubmitting}
                                                label={'Price'}
                                                isInvalid={fieldState.invalid}
                                                errorMessage={fieldState.error?.message}
                                            />
                                        )}
                                    />
                                </div>
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
    );
};

export default ProductForm;