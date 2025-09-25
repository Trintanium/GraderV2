import { useUser } from "@/contexts/UserContext";
import { deleteData, fetchData } from "@/libs/fetchUtils";
import { TestCaseDto } from "@/types/dto";
import { faTrash, faEdit } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";

export default function TestCase() {
  const { user } = useUser();
  const { taskId } = useParams<{ taskId: string }>();
  const queryClient = useQueryClient();

  // Fetch testcases
  const {
    data: testcases = [],
    isLoading: loadingTestcases,
    error: errorTestcases,
  } = useQuery<TestCaseDto[]>({
    queryKey: ["tasks", taskId, "testcases"],
    queryFn: () => fetchData(`/tasks/${taskId}/testcases`),
    enabled: !!taskId,
  });

  // Delete testcase mutation
  const deleteMutation = useMutation({
    mutationFn: (id: number) => deleteData(`/tasks/${taskId}/testcases/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["tasks", taskId, "testcases"],
      });
    },
  });

  // Loading state
  if (loadingTestcases)
    return (
      <div className="flex items-center justify-center h-screen text-gray-500 text-lg">
        Loading...
      </div>
    );

  // Error state
  if (errorTestcases)
    return (
      <div className="flex items-center justify-center h-screen text-red-500 text-lg">
        Error loading testcases: {errorTestcases.message}
      </div>
    );

  return (
    <div className="flex flex-col items-center w-full min-h-screen p-8 bg-[#021526] gap-6 text-white ">
      {/* Header */}
      <div className="flex w-full justify-between items-center mb-6">
        <Link
          to="/tasks"
          className="px-3 py-1 bg-blue-500 text-white rounded shadow hover:bg-blue-600 transition"
        >
          Back
        </Link>

        <h1 className="text-3xl font-bold ">TestCases of Task {taskId}</h1>

        <Link
          to={`/tasks/${taskId}/testcases/new`}
          className="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
        >
          + Add TestCase
        </Link>
      </div>

      {/* TestCases Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full">
        {testcases.length ? (
          testcases.map((tc) => {
            if (
              tc.type === "PUBLIC" ||
              (tc.type === "PRIVATE" && user?.role === "ADMIN")
            ) {
              return (
                <div
                  key={tc.id}
                  className="bg-[#112538] rounded-xl shadow-md p-4 flex flex-col justify-between gap-3 hover:shadow-xl transform hover:scale-105 transition"
                >
                  <div className="text-sm flex justify-between">
                    <span className="font-semibold">TestCase</span>
                    {user?.role === "ADMIN" && (
                      <div className="flex gap-2 mt-auto flex-wrap">
                        <Link
                          to={`/tasks/${taskId}/testcases/${tc.id}/edit`}
                          className="flex-1 px-2 py-1 bg-sky-600 text-white rounded shadow hover:bg-sky-700 transition text-center text-sm"
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Link>
                        <button
                          onClick={() => deleteMutation.mutate(tc.id)}
                          className="flex-1 px-2 py-1 bg-red-600 text-white rounded shadow hover:bg-red-700 transition text-center text-sm"
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </button>
                      </div>
                    )}
                  </div>

                  <div className="text-sm ">
                    <span className="font-semibold">Input:</span>
                    <div>{tc.input}</div>
                  </div>

                  <div className="text-sm ">
                    <span className="font-semibold">Output:</span>
                    <div>{tc.output}</div>
                  </div>

                  <div className="text-sm ">
                    <span className="font-semibold">Type:</span>
                    <div>{tc.type}</div>
                  </div>
                </div>
              );
            }
            return null;
          })
        ) : (
          <div className="col-span-full p-6 text-center text-gray-700 bg-gray-200 rounded-lg">
            No testcases found
          </div>
        )}
      </div>
    </div>
  );
}
