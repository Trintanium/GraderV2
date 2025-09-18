import { createData, fetchData, updateData } from "@/libs/fetchUtils";
import { TestCaseDto } from "@/types/dto";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function TestcaseForm() {
  const { taskId, testcaseId } = useParams<{
    taskId: string;
    testcaseId?: string;
  }>();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  if (!taskId) throw new Error("taskId is missing from URL");

  const [input, setInput] = useState("");
  const [output, setOutput] = useState("");
  const [type, setType] = useState<"PUBLIC" | "PRIVATE">("PUBLIC");

  // Fetch testcase if editing
  const { data: testCase } = useQuery<TestCaseDto>({
    queryKey: ["testcases", testcaseId],
    queryFn: () => fetchData(`/tasks/${taskId}/testcases/${testcaseId}`),
    enabled: !!testcaseId,
  });

  useEffect(() => {
    if (testCase) {
      setInput(testCase.input);
      setOutput(testCase.output);
      setType(testCase.type as "PUBLIC" | "PRIVATE");
    }
  }, [testCase]);

  const mutation = useMutation({
    mutationFn: () => {
      const payload = { input, output, type };
      if (testcaseId) {
        return updateData(`/tasks/${taskId}/testcases/${testcaseId}`, payload);
      }
      return createData(`/tasks/${taskId}/testcases`, payload);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["testcases", taskId] });
      navigate(`/tasks/${taskId}/testcases`);
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate();
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-white rounded shadow">
      <Link
        to={`/tasks/${taskId}/testcases`}
        className="self-start px-2 py-1 bg-blue-500 text-white rounded mb-4 inline-block"
      >
        Back
      </Link>

      <h2 className="text-xl font-bold mb-4">
        {testcaseId ? "Edit Testcase" : "Create Testcase"}
      </h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Input */}
        <div>
          <label className="block mb-1 font-medium">Input</label>
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            className="w-full border rounded px-2 py-1"
            rows={5}
            required
          />
        </div>

        {/* Output */}
        <div>
          <label className="block mb-1 font-medium">Output</label>
          <textarea
            value={output}
            onChange={(e) => setOutput(e.target.value)}
            className="w-full border rounded px-2 py-1"
            rows={5}
            required
          />
        </div>

        {/* Type */}
        <div>
          <label className="block mb-1 font-medium">Type</label>
          <select
            value={type}
            onChange={(e) => setType(e.target.value as "PUBLIC" | "PRIVATE")}
            className="w-full border rounded px-2 py-1"
            required
          >
            <option value="PUBLIC">Public</option>
            <option value="PRIVATE">Private</option>
          </select>
        </div>

        {/* Submit */}
        <div>
          <button
            type="submit"
            className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600"
            disabled={mutation.isPending}
          >
            {mutation.isPending
              ? "Saving..."
              : testcaseId
              ? "Update Testcase"
              : "Create Testcase"}
          </button>
        </div>
      </form>
    </div>
  );
}
