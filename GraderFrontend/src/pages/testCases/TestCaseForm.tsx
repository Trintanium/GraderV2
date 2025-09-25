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
    <div className="bg-[#021526] w-full min-h-screen text-white flex justify-center py-8">
      <div className="w-full max-w-2xl p-4 rounded shadow">
        {/* Breadcrumb */}
        <div className="flex gap-2 py-4 text-sm text-[#CCCCCC]">
          <Link to={`/tasks/${taskId}/testcases`} className="hover:underline">
            All Testcases
          </Link>
          <span>&gt;</span>
          <span>{testcaseId ? "Edit Testcase" : "Add Testcase"}</span>
        </div>

        {/* Form */}
        <form
          onSubmit={handleSubmit}
          className="space-y-5 bg-[#112538] p-6 rounded-2xl border border-[#746F6F]"
        >
          <h2 className="text-2xl font-bold mb-4">
            {testcaseId ? "Edit Testcase" : "Create Testcase"}
          </h2>

          {/* Input */}
          <div>
            <label className="block mb-2 font-medium">Input:</label>
            <textarea
              value={input}
              onChange={(e) => setInput(e.target.value)}
              className="w-full border border-[#746F6F] rounded-lg px-3 py-2 resize-none focus:outline-none focus:ring-2 focus:ring-indigo-500"
              rows={5}
              required
            />
          </div>

          {/* Expected Output */}
          <div>
            <label className="block mb-2 font-medium">Expected Output:</label>
            <textarea
              value={output}
              onChange={(e) => setOutput(e.target.value)}
              className="w-full border border-[#746F6F] rounded-lg px-3 py-2 resize-none focus:outline-none focus:ring-2 focus:ring-indigo-500"
              rows={5}
              required
            />
          </div>

          {/* Type */}
          <div>
            <label className="block mb-2 font-medium">Type:</label>
            <select
              value={type}
              onChange={(e) => setType(e.target.value as "PUBLIC" | "PRIVATE")}
              className="w-full border border-[#746F6F] rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              required
            >
              <option value="PUBLIC">Public</option>
              <option value="PRIVATE">Private</option>
            </select>
          </div>

          {/* Submit & Cancel */}
          <div className="flex flex-col sm:flex-row gap-4 mt-4">
            <button
              type="submit"
              className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition"
              disabled={mutation.isPending}
            >
              {mutation.isPending
                ? "Saving..."
                : testcaseId
                ? "Update Testcase"
                : "Create Testcase"}
            </button>

            <Link
              to={`/tasks/${taskId}/testcases`}
              className="flex-1 bg-green-500 text-white text-center py-2 rounded-lg hover:bg-green-600 transition"
            >
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
