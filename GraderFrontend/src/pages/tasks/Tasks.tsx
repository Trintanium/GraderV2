import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteData, fetchData } from "@/libs/fetchUtils";
import { problemDto, tagDto, problemTagDto } from "@/types/dto";
import { useUser } from "@/contexts/UserContext";
import { Link } from "react-router-dom";

export default function Tasks() {
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const [sortOrder, setSortOrder] = useState<"default" | "asc" | "desc">(
    "default"
  );
  const { user } = useUser();
  const queryClient = useQueryClient();

  // Fetch problems
  const { data: problems = [], isLoading: loadingProblems } = useQuery<
    problemDto[]
  >({
    queryKey: ["problems"],
    queryFn: () => fetchData("/problem"),
  });

  // Fetch tags
  const { data: tags = [], isLoading: loadingTags } = useQuery<tagDto[]>({
    queryKey: ["tags"],
    queryFn: () => fetchData("/tag"),
  });

  // Fetch problem-tags
  const { data: problemTags = [], isLoading: loadingProblemTags } = useQuery<
    problemTagDto[]
  >({
    queryKey: ["problemTag"],
    queryFn: () => fetchData("/problem-tag"),
  });

  const deleteProblemMutation = useMutation({
    mutationFn: (id: number) => deleteData(`/problem/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["problems"] });
      queryClient.invalidateQueries({ queryKey: ["problemTag"] });
    },
  });

  if (loadingProblems || loadingTags || loadingProblemTags)
    return (
      <div className="flex items-center justify-center h-screen text-gray-500 text-lg">
        Loading...
      </div>
    );

  const problemsWithTags = problems.map((p) => {
    const tagsForProblem = problemTags
      .filter((pt) => pt.problemId === p.id)
      .map((pt) => tags.find((t) => t.id === pt.tagId))
      .filter(Boolean) as tagDto[];
    return { ...p, tags: tagsForProblem };
  });

  const filteredProblems = selectedTagIds.length
    ? problemsWithTags.filter((p) =>
        p.tags?.some((t) => selectedTagIds.includes(t.id))
      )
    : problemsWithTags;

  const sortedProblems = filteredProblems.slice().sort((a, b) => {
    if (sortOrder === "asc") return a.title.localeCompare(b.title);
    if (sortOrder === "desc") return b.title.localeCompare(a.title);
    return 0;
  });

  const toggleTag = (tagId: number) => {
    setSelectedTagIds((prev) =>
      prev.includes(tagId)
        ? prev.filter((id) => id !== tagId)
        : [...prev, tagId]
    );
  };

  const difficultyColor = (diff: string) => {
    switch (diff.toLowerCase()) {
      case "easy":
        return "bg-green-100 text-green-800";
      case "medium":
        return "bg-yellow-100 text-yellow-800";
      case "hard":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="flex flex-col items-center w-full min-h-screen p-8 bg-gradient-to-br from-indigo-50 to-indigo-100 gap-6">
      {/* Header */}
      <div className="flex justify-between w-full items-center mb-6">
        <h1 className="text-3xl font-bold text-indigo-900">Tasks</h1>
        {user && (
          <Link
            to="/tasks/new"
            className="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
          >
            + Add Task
          </Link>
        )}
      </div>

      {/* Tag Filter */}
      <div className="w-full mb-6">
        <h2 className="font-semibold text-lg mb-2 text-indigo-800">
          Filter by Tags
        </h2>
        <div className="flex flex-wrap gap-2">
          {tags.map((tag) => (
            <div
              key={tag.id}
              className={`cursor-pointer px-3 py-1 rounded-full text-sm font-medium transition ${
                selectedTagIds.includes(tag.id)
                  ? "bg-indigo-600 text-white shadow"
                  : "bg-gray-300 text-gray-800 hover:bg-gray-400"
              }`}
              onClick={() => toggleTag(tag.id)}
            >
              {tag.name}
            </div>
          ))}
        </div>
      </div>

      {/* Sorting */}
      <div className="flex gap-2 mb-6">
        {(["default", "asc", "desc"] as const).map((order) => (
          <button
            key={order}
            className={`px-4 py-1 rounded-lg text-sm font-medium transition ${
              sortOrder === order
                ? "bg-indigo-600 text-white shadow"
                : "bg-gray-300 text-gray-800 hover:bg-gray-400"
            }`}
            onClick={() => setSortOrder(order)}
          >
            {order.toUpperCase()}
          </button>
        ))}
      </div>

      {/* Problems List */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 w-full">
        {sortedProblems.length ? (
          sortedProblems.map((problem) => (
            <div
              key={problem.id}
              className="bg-white rounded-xl shadow-md p-4 flex flex-col gap-3 hover:shadow-xl transition"
            >
              <Link
                to={`/tasks/${problem.id}`}
                className="text-lg font-bold text-indigo-900 hover:underline"
              >
                {problem.title}
              </Link>

              <div className="flex flex-wrap gap-2">
                {problem.tags?.length ? (
                  problem.tags.map((t) => (
                    <span
                      key={t.id}
                      className="px-2 py-0.5 rounded-full bg-indigo-100 text-indigo-800 text-xs font-medium"
                    >
                      {t.name}
                    </span>
                  ))
                ) : (
                  <span className="text-gray-400 text-xs">No tags</span>
                )}
              </div>

              <div
                className={`w-max px-2 py-1 rounded text-xs font-semibold ${difficultyColor(
                  problem.difficulty
                )}`}
              >
                {problem.difficulty}
              </div>

              {user && (
                <div className="flex gap-2 mt-auto flex-wrap">
                  <Link
                    to={`/tasks/${problem.id}/edit`}
                    className="flex-1 px-2 py-1 bg-sky-600 text-white rounded shadow hover:bg-sky-700 transition text-center text-sm"
                  >
                    Edit
                  </Link>
                  <button
                    onClick={() => deleteProblemMutation.mutate(problem.id)}
                    className="flex-1 px-2 py-1 bg-red-600 text-white rounded shadow hover:bg-red-700 transition text-center text-sm"
                  >
                    Delete
                  </button>
                  <Link
                    to={`/tasks/${problem.id}/testcases`}
                    className="flex-1 px-2 py-1 bg-green-600 text-white rounded shadow hover:bg-green-700 transition text-center text-sm"
                  >
                    TestCases
                  </Link>
                </div>
              )}
            </div>
          ))
        ) : (
          <div className="col-span-full p-6 text-center text-gray-700 bg-gray-200 rounded-lg">
            No problems found
          </div>
        )}
      </div>
    </div>
  );
}
