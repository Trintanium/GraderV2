import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { createData } from "@/libs/fetchUtils";
import { RegisterRequest } from "@/types/auth";

export default function RegisterForm() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const [message, setMessage] = useState("");

  interface ErrorResponse {
    message: string;
  }

  const mutation = useMutation<null, ErrorResponse, RegisterRequest>({
    mutationFn: (data) =>
      createData<null, RegisterRequest>("/auth/signUp", data),
    onSuccess: () => {
      setMessage("Sent Verification Email, Please Check your email...");
    },
    onError: (err) => setMessage(err.message || "Signup failed"),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("Sending verification email...");

    if (email && password) {
      const fullEmail = email.includes("@gmail.com")
        ? email
        : `${email}@gmail.com`;

      mutation.mutate({
        username,
        email: fullEmail,
        password,
        confirmPassword,
      });
    } else {
      setMessage("Please enter email and password");
    }
  };

  return (
    <div className="w-full h-full flex justify-center items-center ">
      <form onSubmit={handleSubmit} className="w-full h-3/5">
        <h1 className="text-5xl mb-6 text-center">Create an Account</h1>
        <div>Username (Display Name)</div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type="text"
            className="w-full p-2 outline-none focus:ring-0 focus:border-none"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <div>Email</div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type="text"
            className="w-full p-2 outline-none "
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <span className="px-2 text-gray-500">@gmail.com</span>
        </div>
        <div>Password</div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type={showPassword ? "text" : "password"}
            className="w-full p-2 outline-none"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <span
            className="px-2  cursor-pointer text-gray-500"
            onClick={() => setShowPassword(!showPassword)}
          >
            <i className={`fas ${showPassword ? "fa-eye" : "fa-eye-slash"}`} />
          </span>
        </div>
        <div>Confirm Password </div>
        <div className="border-2 rounded-md mb-4 flex items-center focus-within:border-sky-500 focus-within:ring-1 focus-within:ring-sky-500 transition">
          <input
            type={showPassword ? "text" : "password"}
            className="w-full p-2 outline-none "
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
          <span
            className="px-2  cursor-pointer text-gray-500"
            onClick={() => setShowPassword(!showPassword)}
          >
            <i className={`fas ${showPassword ? "fa-eye" : "fa-eye-slash"}`} />
          </span>
        </div>
        <button
          type="submit"
          className={`w-full py-2 rounded-md mt-4 text-white ${
            mutation.isPending ? "bg-gray-400" : "bg-sky-800"
          }`}
          disabled={mutation.isPending}
        >
          {mutation.isPending ? "Sending..." : "Register"}
        </button>
        {message && (
          <div className="text-center mb-2 text-blue-500">{message}</div>
        )}
      </form>
    </div>
  );
}
