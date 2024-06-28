import React from 'react';
import {SignInForm} from "@/app/ui/forms/sign-in-form";
import {auth} from "@/auth";
import {redirect} from "next/navigation";

const LoginPage = async () => {
    const session = await auth();

    if (session) {
        redirect('/');
    }

    return (
        <div>
            <SignInForm/>
        </div>
    );
};

export default LoginPage;