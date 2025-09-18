import { useEffect } from "react";

interface ModalProps {
  message: string;
  status: "success" | "error";
  onClose: () => void;
}

export default function Modal({ message, status, onClose }: ModalProps) {
  const color = status === "success" ? "bg-green-500" : "bg-red-500";

  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 2000);
    return () => clearTimeout(timer);
  }, [onClose]);

  return (
    <div className="fixed inset-0 flex justify-center items-center backdrop-blur-xl">
      <div className={`px-6 py-4 text-white rounded shadow-lg ${color}`}>
        {message}
      </div>
    </div>
  );
}
