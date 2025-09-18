import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { createData } from "@/libs/fetchUtils";
import CodeEditor from "@/components/tasks/CodeEditer";
import Modal from "@/components/Modal";
import Dropdown from "@/components/Dropdown";

interface SubmitFormProps {
  problemId: number;
  onSuccess?: () => void;
}

export default function SubmitForm({ problemId, onSuccess }: SubmitFormProps) {
  const [code, setCode] = useState("");
  const [modal, setModal] = useState<{
    message: string;
    status: "success" | "error";
  } | null>(null);
  const [language, setLanguage] = useState("");

  const languageList = ["PYTHON", "JAVASCRIPT", "JAVA", "C"];

  const sendSubmission = useMutation({
    mutationFn: () =>
      createData("/submissions", { problemId, code, language: language }),
    onSuccess: () => {
      setModal({ message: "Submission sent successfully!", status: "success" });
      setCode("");
      onSuccess?.();
    },
    onError: () => {
      setModal({ message: "Error sending submission", status: "error" });
    },
  });

  const handleSubmit = () => {
    if (!code.trim())
      return setModal({ message: "Cannot submit empty code", status: "error" });
    if (!language)
      return setModal({
        message: "Please select Programming Language",
        status: "error",
      });
    sendSubmission.mutate();
  };

  return (
    <div className="flex flex-col h-full gap-2">
      <div className="w-1/2 ">
        <Dropdown
          list={languageList}
          label="Choose Language"
          onChange={setLanguage}
        />
      </div>
      <CodeEditor value={code} onChange={setCode} />
      <button
        className="px-4 py-2 bg-blue-500 text-white rounded self-end"
        onClick={handleSubmit}
        disabled={sendSubmission.isPending}
      >
        {sendSubmission.isPending ? "Submitting..." : "Submit"}
      </button>
      {modal && (
        <Modal
          message={modal.message}
          status={modal.status}
          onClose={() => setModal(null)}
        />
      )}
    </div>
  );
}
