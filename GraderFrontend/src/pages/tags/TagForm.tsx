import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { fetchData, createData, updateData } from "@/libs/fetchUtils";
import { tagDto } from "@/types/dto";

export default function TagForm() {
  const { tagId } = useParams<{ tagId: string }>();
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const [name, setName] = useState("");

  // Fetch tag if editing
  const { data: tag } = useQuery<tagDto>({
    queryKey: ["tags", tagId],
    queryFn: () => fetchData(`/tags/${tagId}`),
    enabled: !!tagId,
  });

  useEffect(() => {
    if (tag) {
      setName(tag.name);
    }
  }, [tag]);

  // Mutation for create/update
  const mutation = useMutation({
    mutationFn: async () => {
      if (tagId) {
        return updateData(`/tags/${tagId}`, { name });
      } else {
        return createData("/tags", { name });
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tags"] });
      navigate("/tags");
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate();
  };

  return (
    <div className="max-w-md mx-auto p-4 bg-white rounded shadow">
      <Link
        to="/tags"
        className="self-start px-2 py-1 bg-blue-500 text-white rounded mb-4 inline-block"
      >
        Back
      </Link>
      <h2 className="text-xl font-bold mb-4">
        {tagId ? "Edit Tag" : "Create Tag"}
      </h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Title */}
        <div>
          <label className="block mb-1 font-medium">Title</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full border rounded px-2 py-1"
            required
          />
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
              : tagId
              ? "Update Tag"
              : "Create Tag"}
          </button>
        </div>
      </form>
    </div>
  );
}
