import { useState, useEffect } from "react";
import { useMutation } from "@tanstack/react-query";
import { Link, useNavigate } from "react-router-dom";
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

  // Initialize user data
  useEffect(() => {
    if (user) {
      setUsername(user.username || "");
      setPreview(user.profilePicture || null);
    }
  }, [user]);

  // Mutation for updating user profile
  const mutation = useMutation<UserDto, Error>({
    mutationFn: async () => {
      const formData = new FormData();
      if (username.trim()) formData.append("username", username);
      if (file) formData.append("png", file);

      return updateFormData("/user/update", formData);
    },
    onSuccess: (updatedUser) => {
      setUser(updatedUser);
      setPreview(updatedUser.profilePicture || null);

      setModalMessage("Profile updated successfully!");
      setModalStatus("success");
      setShowModal(true);

      setTimeout(() => navigate("/"), 2000);
    },
    onError: () => {
      setModalMessage("Update failed. Please try again!");
      setModalStatus("error");
      setShowModal(true);
    },
  });

  if (!user)
    return <div className="text-center mt-10 text-lg">Loading user...</div>;

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0];
    if (selected) {
      setFile(selected);
      setPreview(URL.createObjectURL(selected));
    }
  };

  return (
    <div className="flex flex-col items-center w-full min-h-screen p-6 bg-[#021526] text-white">
      <div className="w-full max-w-3xl bg-[#112538] shadow-xl rounded-2xl p-8">
        <h1 className="text-3xl font-bold mb-6 ">Profile Settings</h1>

        <div className="flex flex-col md:flex-row gap-8">
          {/* Profile Picture Section */}
          <div className="flex flex-col items-center gap-3 md:w-1/2">
            <div className="relative w-32 h-32">
              <img
                src={preview || Anonymous}
                alt="Profile"
                className="w-32 h-32 rounded-full object-cover border-4 border-indigo-300 shadow-md"
              />
            </div>
            <label className="px-3 py-1 bg-indigo-600 text-white text-sm rounded-lg shadow hover:bg-indigo-700 cursor-pointer transition">
              Change Photo
              <input
                type="file"
                accept="image/png"
                onChange={handleFileChange}
                className="hidden"
              />
            </label>
          </div>

          {/* User Info Section */}
          <div className="flex flex-col gap-4 md:w-1/2">
            <div>
              <label className="block text-sm font-semibold mb-1 ">
                Username
              </label>
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full px-3 py-2  border border-[#746F6F] rounded-lg focus:ring-2 focus:ring-indigo-400 focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-semibold mb-1 ">Email</label>
              <div className="w-full px-3 py-2   border border-[#746F6F] rounded-lg ">
                {user.email}
              </div>
            </div>

            {/* Buttons */}
            <div className="flex gap-3 mt-2">
              <button
                onClick={() => mutation.mutate()}
                className="px-4 py-2 bg-[#03346E] text-white rounded-lg shadow hover:bg-green-700 transition border border-[#746F6F]"
              >
                Save Changes
              </button>
              <Link
                to="/"
                className="px-4 py-2 bg-[#112538] text-white rounded-lg shadow hover:bg-gray-500 transition border border-[#746F6F]"
              >
                Cancel
              </Link>
            </div>

            {mutation.isPending && (
              <div className="text-sm text-indigo-600 mt-2">Updating...</div>
            )}
          </div>
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
    </div>
  );
}
