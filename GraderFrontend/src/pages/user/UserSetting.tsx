import { useState, useEffect } from "react";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { updateFormData } from "@/libs/fetchUtils";
import { useUser } from "@/contexts/UserContext";
import { UserDto } from "@/types/dto";
import Modal from "@/components/Modal";
import Anonymous from "@/assets/Anonymous.png";

export default function UserSettings() {
  const { user, setUser } = useUser();
  const [username, setUsername] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);

  const [modalMessage, setModalMessage] = useState("");
  const [modalStatus, setModalStatus] = useState<"success" | "error">(
    "success"
  );
  const [showModal, setShowModal] = useState(false);

  const navigate = useNavigate();

  // Initialize state
  useEffect(() => {
    if (user) {
      setUsername(user.username || "");
      setPreview(user.profilePicture || null);
    }
  }, [user]);

  // Mutation to update user
  const mutation = useMutation<UserDto, Error>({
    mutationFn: async () => {
      const formData = new FormData();
      if (username.trim()) formData.append("username", username);
      if (file) formData.append("png", file); // optional

      return updateFormData("/user/update", formData); // use FormData PUT
    },
    onSuccess: (updatedUser: UserDto) => {
      setUser(updatedUser);
      setPreview(updatedUser.profilePicture || null);

      setModalMessage("Updated successfully!");
      setModalStatus("success");
      setShowModal(true);

      setTimeout(() => navigate("/"), 2000); // redirect to home
    },
    onError: () => {
      setModalMessage("Update failed!");
      setModalStatus("error");
      setShowModal(true);
    },
  });

  if (!user) return <div>Loading user...</div>;

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0];
    if (selected) {
      setFile(selected);
      setPreview(URL.createObjectURL(selected));
    }
  };

  return (
    <div className="flex flex-col items-center w-4/5 min-h-screen p-8 bg-amber-100 gap-6">
      <h1 className="text-3xl font-bold mb-4">User Settings</h1>

      <div className="flex flex-col gap-4 w-full max-w-md">
        {/* Profile Picture */}
        <div className="flex flex-col items-center gap-2">
          <img
            src={preview || Anonymous}
            alt="Profile"
            className="w-32 h-32 rounded-full object-cover border border-gray-400"
          />
          <input type="file" accept="image/png" onChange={handleFileChange} />
        </div>
        {/* Username */}
        <div className="flex flex-col gap-1">
          <label className="font-semibold">Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="px-3 py-2 border border-gray-400 rounded"
          />
        </div>

        {/* Save */}
        <button
          onClick={() => mutation.mutate()}
          className="px-4 py-2 bg-green-600 text-white rounded-lg shadow hover:bg-green-700 transition"
        >
          Save Changes
        </button>

        {mutation.isPending && <div>Updating...</div>}
      </div>

      {/* Modal */}
      {showModal && (
        <Modal
          message={modalMessage}
          status={modalStatus}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
}
