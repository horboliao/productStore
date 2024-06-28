'use client'
import React, {useState} from 'react';
import {Controller, useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Button, Input} from "@nextui-org/react";
import {handleLogin, login} from "@/actions/login";

const SignInFormSchema = z.object({
    email: z.string().min(1, 'Invalid email address'),
    password: z.string().min(1, 'Password must be at least 6 characters long'),
});

export function SignInForm() {
    const form = useForm<z.infer<typeof SignInFormSchema>>({resolver: zodResolver(SignInFormSchema)});

    const { handleSubmit, control } = form;
    const { isSubmitting, isValid } = form.formState;

    const onSubmit = async (values: z.infer<typeof SignInFormSchema>) => {
        console.log(values)
        await handleLogin(values.email, values.password)

        // try {
        //     await axios.patch(`/api/courses/${courseId}`, values);
        //     toast.success("Назву курсу оновлено");
        //     toggleEdit();
        //     router.refresh();
        // } catch {
        //     toast.error("Не вдалось оновити назву курс");
        // }
    }

    return (
        <div className="flex flex-col w-full max-w-sm p-4">
            {/*<Form {...form}>*/}
            {/*    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">*/}
            {/*        <FormItem>*/}
            {/*            <FormControl>*/}
            {/*                <div className="grid w-full max-w-sm items-center gap-1.5">*/}
            {/*                    <Label htmlFor="email">Email</Label>*/}
            {/*                    <Input id="email" placeholder="example@example.com" {...form.register('email')} />*/}
            {/*                </div>*/}
            {/*            </FormControl>*/}
            {/*            <FormMessage />*/}
            {/*        </FormItem>*/}
            {/*        <FormItem>*/}
            {/*            <FormControl>*/}
            {/*                <div className="grid w-full max-w-sm items-center gap-1.5">*/}
            {/*                    <Label htmlFor="password">Password</Label>*/}
            {/*                    <Input id="password" type="password" placeholder="********" {...form.register('password')} />*/}
            {/*                </div>*/}
            {/*            </FormControl>*/}
            {/*            <FormMessage />*/}
            {/*        </FormItem>*/}
            {/*        <Button type="submit" disabled={loading} className='w-full'>*/}
            {/*            {loading ? 'Signing In...' : 'Sign In'}*/}
            {/*        </Button>*/}
            {/*    </form>*/}
            {/*</Form>*/}
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="space-y-4 mt-4"
            >
                <Controller
                    name="email"
                    control={control}
                    render={({ field, fieldState }) => (
                        <Input
                            {...field}
                            isRequired
                            disabled={isSubmitting}
                            label={'Email'}
                            isInvalid={fieldState.invalid}
                            errorMessage={fieldState.error?.message}
                        />
                    )}
                />
                <Controller
                    name="password"
                    control={control}
                    render={({ field, fieldState }) => (
                        <Input
                            {...field}
                            isRequired
                            disabled={isSubmitting}
                            label={'Password'}
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
                        Login
                    </Button>
                </div>
            </form>
        </div>
    );
}
