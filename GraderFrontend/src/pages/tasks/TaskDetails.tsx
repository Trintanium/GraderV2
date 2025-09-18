import { fetchData } from "@/libs/fetchUtils";
import { problemDto, submissionDto } from "@/types/dto";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useUser } from "@/contexts/UserContext";
import SubmitForm from "@/components/tasks/SubmitForm";
import Submissions from "@/components/tasks/Submissions";
import MySubmissions from "@/components/tasks/MySubmissions";

type TabKey = "statement" | "submit" | "submissions" | "mySubmissions";

export default function TaskDetails() {
  const { taskId } = useParams<{ taskId: string }>();
  const { user } = useUser();
  const [tab, setTab] = useState<TabKey>("statement");

  const tabs: { key: TabKey; label: string }[] = [
    { key: "statement", label: "Statement" },
    { key: "submit", label: "Submit" },
    { key: "submissions", label: "Submissions" },
    { key: "mySubmissions", label: "My Submissions" },
  ];

  // Problem Data
  const {
    data: problem,
    isLoading: loadingProblem,
    error: errorProblem,
  } = useQuery<problemDto>({
    queryKey: ["problem", taskId],
    queryFn: () => fetchData(`/problem/${taskId}`),
    enabled: !!taskId,
  });

  // My Submissions
  const {
    data: mySubmission = [],
    isLoading: loadingMySubmission,
    error: errorMySubmission,
    refetch: refetchMySubmission,
  } = useQuery<submissionDto[]>({
    queryKey: ["submissionsMy", taskId],
    queryFn: () => fetchData(`/submissions/my?problemId=${taskId}`),
    enabled: !!taskId,
  });

  // All Submissions
  const {
    data: allSubmissions = [],
    isLoading: loadingAllSubmissions,
    error: errorAllSubmissions,
    refetch: refetchAllSubmissions,
  } = useQuery<submissionDto[]>({
    queryKey: ["submissionsAll", taskId],
    queryFn: () => fetchData(`/submissions/all?problemId=${taskId}`),
    enabled: !!taskId,
  });

  if (loadingProblem || loadingMySubmission || loadingAllSubmissions)
    return (
      <div className="flex items-center justify-center h-screen text-gray-500 text-lg">
        Loading...
      </div>
    );
  if (errorProblem || errorMySubmission || errorAllSubmissions)
    return (
      <div className="flex items-center justify-center h-screen text-red-600 text-lg">
        Error loading data
      </div>
    );

  const maxScore = mySubmission.length
    ? Math.max(...mySubmission.map((s) => s.score))
    : 0;

  return (
    <div className="flex flex-col w-full h-screen bg-gradient-to-br from-indigo-50 to-indigo-100 p-6 gap-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-4xl font-extrabold text-indigo-900">
          Task Details
        </h1>
        <Link
          to="/tasks"
          className="px-4 py-2 bg-indigo-600 text-white font-medium rounded shadow hover:bg-indigo-700 transition"
        >
          Back to Tasks
        </Link>
      </div>

      {/* Main container */}
      <div className="flex flex-1 gap-6 overflow-hidden">
        {/* Sidebar */}
        <div className="w-1/4 flex flex-col gap-4 p-4 bg-white rounded-xl shadow-md border border-indigo-200">
          <div className="text-sm text-gray-500">Task ID: {taskId}</div>
          <div className="text-xl font-semibold text-indigo-800">
            {problem?.title}
          </div>
          <div className="border-t border-indigo-200 my-2" />

          {/* Tabs */}
          <div className="flex flex-col gap-2">
            {tabs.map((t) => (
              <button
                key={t.key}
                className={`text-left px-3 py-2 rounded-md font-medium transition ${
                  tab === t.key
                    ? "bg-indigo-600 text-white shadow"
                    : "hover:bg-indigo-100 text-indigo-800"
                }`}
                onClick={() => setTab(t.key)}
              >
                {t.label}
              </button>
            ))}
          </div>

          <div className="border-t border-indigo-200 my-2" />
          <div className="text-sm text-gray-700 font-medium">
            Highest Score: {maxScore}
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 flex flex-col bg-white rounded-xl shadow-md p-4 overflow-hidden">
          {/* Statement PDF */}
          {tab === "statement" && problem?.pdfUrl && (
            <embed
              src={problem.pdfUrl}
              type="application/pdf"
              className="flex-1 w-full rounded border border-indigo-200"
            />
          )}

          {/* Submit Form */}
          {tab === "submit" &&
            (user ? (
              <div className="flex-1 overflow-auto">
                <SubmitForm
                  problemId={Number(taskId)}
                  onSuccess={() => {
                    setTimeout(() => {
                      refetchMySubmission();
                      refetchAllSubmissions();
                    }, 5000);
                  }}
                />
              </div>
            ) : (
              <div className="text-center text-red-600 mt-6 font-medium">
                Please login to submit code
              </div>
            ))}

          {/* All Submissions */}
          {tab === "submissions" && (
            <div className="flex-1 overflow-auto">
              <Submissions submission={allSubmissions} />
            </div>
          )}

          {/* My Submissions */}
          {tab === "mySubmissions" && (
            <div className="flex-1 overflow-auto">
              <MySubmissions submission={mySubmission} />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
