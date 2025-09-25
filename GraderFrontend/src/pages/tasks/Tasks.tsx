import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteData, fetchData } from "@/libs/fetchUtils";
import { problemDto, tagDto, problemTagDto } from "@/types/dto";
import { useUser } from "@/contexts/UserContext";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faTrash,
  faBook,
  faSort,
  faSortUp,
  faSortDown,
  faMagnifyingGlass,
} from "@fortawesome/free-solid-svg-icons";
export default function Tasks() {
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const [selectedDifficulty, setSelectedDifficulty] = useState<string | null>(
    null
  );
  const [sortOrder, setSortOrder] = useState<"default" | "asc" | "desc">(
    "default"
  );
  const [searchKeyword, setSearchKeyword] = useState("");

  const { user } = useUser();
  const queryClient = useQueryClient();

  const difficulty = [
    { name: "Beginner", value: "EASY" },
    { name: "Intermediate", value: "MEDIUM" },
    { name: "Advanced", value: "HARD" },
  ];

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

  // Add tags to each problem
  const problemsWithTags = problems.map((p) => {
    const tagsForProblem = problemTags
      .filter((pt) => pt.problemId === p.id)
      .map((pt) => tags.find((t) => t.id === pt.tagId))
      .filter(Boolean) as tagDto[];
    return { ...p, tags: tagsForProblem };
  });

  // Filtering logic
  const filteredProblems = problemsWithTags.filter((p) => {
    const matchesTags =
      selectedTagIds.length === 0 ||
      p.tags?.some((t) => selectedTagIds.includes(t.id));

    const matchesDifficulty =
      !selectedDifficulty || p.difficulty === selectedDifficulty;

    const matchesSearch =
      !searchKeyword ||
      p.title.toLowerCase().includes(searchKeyword.toLowerCase());

    return matchesTags && matchesDifficulty && matchesSearch;
  });

  // Sorting logic
  const sortedProblems = filteredProblems.slice().sort((a, b) => {
    if (sortOrder === "asc") return a.title.localeCompare(b.title);
    if (sortOrder === "desc") return b.title.localeCompare(a.title);
    return 0;
  });

  // Toggle functions
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
        return "bg-green-800 text-white border border-green-400 px-4 py-1 rounded-xl";
      case "medium":
        return "bg-yellow-800 text-white border border-yellow-400 px-4 py-1 rounded-xl";
      case "hard":
        return "bg-red-800 text-white border border-red-400 px-4 py-1 rounded-xl";
      default:
        return "bg-gray-600 text-white border border-grey-400 px-4 py-1 rounded-xl";
    }
  };

  return (
    <div className="flex  w-full min-h-screen p-8 bg-[#021526] gap-6">
      {/* Sidebar Filters */}
      <div className="w-1/4 flex flex-col gap-6">
        <div className="flex justify-between w-full items-center mb-6">
          <h1 className="text-3xl font-bold  text-white">Tasks</h1>
        </div>

        {/* Difficulty filter (radio) */}
        <div className="mb-6 bg-[#112538] text-white p-4 rounded-2xl border border-[#746F6F]">
          <h2 className="font-semibold text-lg mb-2 ">Difficulty Level</h2>
          <div className="flex flex-col gap-2 ">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="radio"
                name="difficulty"
                value=""
                checked={selectedDifficulty === null}
                onChange={() => setSelectedDifficulty(null)}
                className="w-4 h-4 accent-indigo-600"
              />
              <span>All Levels</span>
            </label>
            {difficulty.map((diff) => (
              <label
                key={diff.value}
                className="flex items-center gap-2 cursor-pointer"
              >
                <input
                  type="radio"
                  name="difficulty"
                  value={diff.value}
                  checked={selectedDifficulty === diff.value}
                  onChange={() => setSelectedDifficulty(diff.value)}
                  className="w-4 h-4 accent-indigo-600"
                />
                <div className="flex gap-2">
                  {diff.name}
                  <div
                    className={`w-max  text-xs font-semibold ${difficultyColor(
                      diff.value
                    )}`}
                  >
                    {diff.value}
                  </div>{" "}
                </div>
              </label>
            ))}
          </div>
        </div>
        {/* Tag filter (checkboxes) */}
        <div className="bg-[#112538] text-white p-4 rounded-2xl border border-[#746F6F]">
          <h2 className="font-semibold text-lg mb-2 ">Filter by Tags</h2>
          <div className="flex flex-col gap-2">
            {tags.map((tag) => (
              <label
                key={tag.id}
                className="flex items-center gap-2 cursor-pointer"
              >
                <input
                  type="checkbox"
                  checked={selectedTagIds.includes(tag.id)}
                  onChange={() => toggleTag(tag.id)}
                  className="w-4 h-4 accent-indigo-600"
                />
                <span>{tag.name}</span>
              </label>
            ))}
          </div>
        </div>
      </div>

      {/* problem Util */}
      <div className="w-3/4 flex flex-col">
        {/* Sorting */}
        <div className="flex gap-2 mb-6 items-center text-white">
          <div className="flex items-center w-full border rounded-2xl border-[#17548B] px-3 h-12">
            <FontAwesomeIcon
              icon={faMagnifyingGlass}
              className="text-gray-500 mr-2"
            />
            <input
              type="text"
              placeholder="Search"
              className="flex-1 outline-none bg-transparent"
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
            />
          </div>
          <button
            onClick={() => setSortOrder("default")}
            className={`px-3 py-2 rounded-lg text-sm font-medium transition ${
              sortOrder === "default"
                ? "bg-[#17548B]"
                : "bg-[#112538] hover:bg-[#7facd6]"
            }`}
            title="Default"
          >
            <FontAwesomeIcon icon={faSort} />
          </button>
          <button
            onClick={() => setSortOrder("asc")}
            className={`px-3 py-2 rounded-lg text-sm font-medium transition ${
              sortOrder === "asc"
                ? "bg-[#17548B]"
                : "bg-[#112538]  hover:bg-[#7facd6]"
            }`}
            title="Ascending"
          >
            <FontAwesomeIcon icon={faSortUp} />
          </button>
          <button
            onClick={() => setSortOrder("desc")}
            className={`px-3 py-2 rounded-lg text-sm font-medium transition ${
              sortOrder === "desc"
                ? "bg-[#17548B]"
                : "bg-[#112538] hover:bg-[#7facd6]"
            }`}
            title="Descending"
          >
            <FontAwesomeIcon icon={faSortDown} />
          </button>

          {user && (
            <Link
              to="/tasks/new"
              className="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
            >
              + Add Task
            </Link>
          )}
        </div>

        {/* Problems Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 w-full text-white">
          {sortedProblems.length ? (
            sortedProblems.map((problem) => (
              <div
                key={problem.id}
                className="bg-[#112538] border border-[#746F6F] rounded-xl shadow-md p-4 flex flex-col gap-3 hover:shadow-xl transition"
              >
                <div className="flex justify-between">
                  <Link
                    to={`/tasks/${problem.id}`}
                    className="text-lg font-bold hover:underline"
                  >
                    {problem.title}
                  </Link>
                  {user && (
                    <div className="flex  gap-4">
                      <Link
                        to={`/tasks/${problem.id}/edit`}
                        className="  text-white text-center text-xl"
                      >
                        <FontAwesomeIcon icon={faEdit} />
                      </Link>
                      <button
                        onClick={() => deleteProblemMutation.mutate(problem.id)}
                        className="   text-red-400 text-center text-xl"
                      >
                        <FontAwesomeIcon icon={faTrash} />
                      </button>
                      <Link
                        to={`/tasks/${problem.id}/testcases`}
                        className="  text-white text-center text-xl"
                      >
                        <FontAwesomeIcon icon={faBook} />
                      </Link>
                    </div>
                  )}
                </div>

                <div
                  className={`w-max text-xs font-semibold ${difficultyColor(
                    problem.difficulty
                  )}`}
                >
                  {problem.difficulty}
                </div>
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
              </div>
            ))
          ) : (
            <div className="col-span-full p-6 text-center text-gray-700 bg-gray-200 rounded-lg">
              No problems found
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
