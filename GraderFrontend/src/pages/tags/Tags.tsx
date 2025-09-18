import { useUser } from "@/contexts/UserContext";
import { deleteData, fetchData } from "@/libs/fetchUtils";
import { tagDto } from "@/types/dto";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link } from "react-router-dom";

export default function Tags() {
  const { user } = useUser();
  const queryClient = useQueryClient();

  const {
    data: tags = [],
    isLoading: loadingTags,
    error: errorTags,
  } = useQuery<tagDto[]>({
    queryKey: ["tags"],
    queryFn: () => fetchData("/tag"),
  });

  const deleteTagMutation = useMutation({
    mutationFn: (id: number) => deleteData(`/tag/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tags"] });
    },
  });

  if (loadingTags)
    return (
      <div className="flex items-center justify-center h-screen text-gray-500 text-lg">
        Loading...
      </div>
    );
  if (errorTags)
    return (
      <div className="flex items-center justify-center h-screen text-red-500 text-lg">
        Error loading tags: {errorTags.message}
      </div>
    );

  return (
    <div className="flex flex-col items-center w-full min-h-screen p-8 bg-gradient-to-br from-indigo-50 to-indigo-100 gap-6">
      {/* Header */}
      <div className="flex justify-between w-full items-center mb-6">
        <h1 className="text-3xl font-bold text-indigo-900">Tags</h1>
        {user && (
          <Link
            to="/tags/new"
            className="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
          >
            + Add Tag
          </Link>
        )}
      </div>

      {/* Tags List */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 w-full">
        {tags.length ? (
          tags.map((tag) => (
            <div
              key={tag.id}
              className="bg-white rounded-xl shadow-md p-4 flex flex-col justify-between hover:shadow-xl transform hover:scale-105 transition"
            >
              <div className="text-lg font-semibold text-indigo-900 mb-3">
                {tag.name}
              </div>
              {user && (
                <div className="flex gap-2 mt-auto flex-wrap">
                  <Link
                    to={`/tags/${tag.id}/edit`}
                    className="flex-1 px-2 py-1 bg-sky-600 text-white rounded shadow hover:bg-sky-700 transition text-center text-sm"
                  >
                    Edit
                  </Link>
                  <button
                    onClick={() => deleteTagMutation.mutate(tag.id)}
                    className="flex-1 px-2 py-1 bg-red-600 text-white rounded shadow hover:bg-red-700 transition text-center text-sm"
                  >
                    Delete
                  </button>
                </div>
              )}
            </div>
          ))
        ) : (
          <div className="col-span-full p-6 text-center text-gray-700 bg-gray-200 rounded-lg">
            No tags found
          </div>
        )}
      </div>
    </div>
  );
}
