import NextAuth from "next-auth"
import Credentials from "next-auth/providers/credentials"
import {login} from "@/actions/login";

export const { handlers, signIn, signOut, auth } = NextAuth({
    providers: [
        Credentials({
            credentials: {
                login: {},
                password: {},
            },
            authorize: async (credentials) => {
                let user = null

                user = await login(credentials.login, credentials.password)

                if (!user) {
                    throw new Error("User not found.")
                }

                return user
            },
        }),
    ],
    secret: process.env.AUTH_SECRET,
    session: {
        maxAge: 1500
    },
    callbacks: {
        async jwt({ token, user }) {
            if (user) {
                token.user = user;
            }
            return token;
        },
        async session({ session, token }) {
            session.user = token.user;
            return session;
        },
    },
})