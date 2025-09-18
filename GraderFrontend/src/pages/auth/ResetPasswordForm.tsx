import { createData } from "@/libs/fetchUtils";
import { useMutation } from "@tanstack/react-query";
import { useState } from "react";
import { AuthResponse } from "@/types/auth";
export default function ResetPasswordForm() {
  const [email, setEmail] = useState("");
  const [essage, setMessage] = useState("");
  const mutation = useMutation<AuthResponse, Error, { email: string }>({
    mutationFn: (data) =>
      createData<AuthResponse, { email: string }>(
        "/auth/password/forgot",
        data
      ),
    onSuccess: () => {
      setMessage("If the email exists, a reset link has been sent.");
    },
    onError: () => {
      setMessage("Failed to send reset link. Please try again.");
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("Sending verification email...");

    if (email) {
      const fullEmail = email.includes("@gmail.com")
        ? email
        : `${email}@gmail.com`;

      mutation.mutate({ email: fullEmail });
    } else {
      setMessage("Please enter email and password");
    }
  };
  return (
    <>
      <div>
        <form
          action=""
          onSubmit={handleSubmit}
          className="w-80 p-6 border rounded-md shadow-md"
        >
          <h1 className="text-3xl mb-6 text-center">ResetPassword</h1>
          <div>Email</div>
          <div className="border-2 rounded-md mb-4 flex items-center">
            <input
              type="text"
              className="w-full p-2 outline-none focus:ring-0 focus:border-none"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <span className="px-2 text-gray-500">@gmail.com</span>
          </div>
          <button
            type="submit"
            className="bg-sky-800 text-white w-full py-2 rounded-md mt-4"
          >
            ForgotPassword
          </button>
          {essage && (
            <div className="text-center mt-4 text-red-600">{essage}</div>
          )}
        </form>
      </div>
    </>
  );
}
