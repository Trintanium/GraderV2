import { useState } from "react";
import LoginForm from "@/components/auth/LoginForm";
import RegisterForm from "@/components/auth/RegisterForm";
import Programer2 from "@/assets/Programer2.png";
export default function AuthPage() {
  const [isLogin, setIsLogin] = useState(false);

  return (
    <div className="relative w-screen h-screen flex items-center justify-center overflow-hidden bg-gradient-to-br from-[#a072dc] to-[#25063d] text-white">
      <div className="relative flex min-w-sm sm:min-w-xl md:min-w-2xl lg:min-w-5xl h-4/5 rounded-xl shadow-2xl bg-[#1F0D4A]/50 overflow-hidden m-48">
        <div className="relative flex w-full h-full overflow-hidden">
          <div
            className={`absolute w-full h-full transition-transform duration-500 flex items-center justify-center ${
              isLogin ? "-translate-x-full" : "translate-x-0"
            }`}
          >
            <div className="w-full flex flex-row items-center gap-24 p-24">
              <RegisterForm />
              <img
                src={Programer2}
                alt="programmer"
                className="w-1/2 h-auto hidden lg:inline"
              />
            </div>
          </div>
          <div
            className={`absolute w-full h-full transition-transform duration-500 flex items-center justify-center ${
              isLogin ? "translate-x-0" : "translate-x-full"
            }`}
          >
            <div className="w-full flex flex-row items-center gap-24 p-24">
              <img
                src={Programer2}
                alt="programmer"
                className="w-1/2 h-auto hidden lg:inline"
              />
              <LoginForm />
            </div>
          </div>
        </div>
        {/* Toggle Buttons */}
        <div className="absolute top-8 left-1/2 -translate-x-1/2 flex rounded-full overflow-hidden border-4 border-white shadow-lg">
          <button
            onClick={() => setIsLogin(false)}
            className={`px-6 py-2 font-semibold transition-colors duration-300 ${
              !isLogin
                ? "bg-white text-[#1F0D4A]"
                : "bg-[#1F0D4A] text-white hover:bg-white hover:text-[#1F0D4A]"
            }`}
          >
            SignUp
          </button>
          <button
            onClick={() => setIsLogin(true)}
            className={`px-6 py-2 font-semibold transition-colors duration-300 ${
              isLogin
                ? "bg-white text-[#1F0D4A]"
                : "bg-[#1F0D4A] text-white hover:bg-white hover:text-[#1F0D4A]"
            }`}
          >
            Login
          </button>
        </div>
      </div>
    </div>
  );
}
