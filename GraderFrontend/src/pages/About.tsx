import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGithub, faInstagram } from "@fortawesome/free-brands-svg-icons";
import Trin from "@/assets/Trin.jpg";
import Proud from "@/assets/Proud.jpg";
import Mission from "@/assets/Mission.jpg";
function About() {
  return (
    <div className="w-full min-h-screen text-white">
      {/* Hero / About Us */}
      <div className="h-screen flex flex-col justify-center items-center bg-[#0A3160] px-8 text-center">
        <h1 className="text-5xl font-bold mb-4">About Us</h1>
        <p className="max-w-2xl text-lg">
          We are Grader, a platform dedicated to helping programmers improve
          their coding skills through real-world problems, automated test cases,
          and instant feedback.
        </p>
      </div>

      {/* Mission Section */}
      <div className="h-screen flex flex-col md:flex-row items-center bg-[#021526] px-8 py-16 gap-8">
        <div className="md:w-1/2 flex flex-col gap-4">
          <h2 className="text-3xl font-bold">Our Mission</h2>
          <p className="text-lg">
            Our mission is to provide a learning environment where programmers
            of all levels can practice, learn, and track their coding progress
            with engaging challenges and automatic evaluation.
          </p>
        </div>
        <img
          src={Mission}
          alt="Mission"
          className="md:w-1/2 h-64 rounded-lg object-cover"
        />
      </div>

      {/* Team Section */}
      <div className="min-h-[70vh] flex flex-col items-center justify-center bg-[#112538] px-8 py-12 ">
        <h2 className="text-3xl font-bold mb-8">Meet Our Team</h2>
        <div className="flex flex-col md:flex-row justify-center items-center gap-12">
          {/* Team Member 1 */}
          <div className="flex flex-col items-center gap-3">
            <div className="w-32 h-32 rounded-full overflow-hidden">
              <img
                src={Trin}
                alt="Trin Meesuannil"
                className="w-full h-full object-cover"
              />
            </div>
            <h3 className="text-lg font-semibold">Trin Meesuannil</h3>
            <p className="text-gray-300 text-sm">FullStack Developer</p>
            <div className="flex gap-4 mt-2">
              <a
                href="https://github.com/Trintanium"
                target="_blank"
                className="text-gray-300 hover:text-white"
              >
                <FontAwesomeIcon icon={faGithub} size="lg" />
              </a>
              <a
                href="https://www.instagram.com/_n3bul4/"
                target="_blank"
                className="text-gray-300 hover:text-white"
              >
                <FontAwesomeIcon icon={faInstagram} size="lg" />
              </a>
            </div>
          </div>

          {/* Team Member 2 */}
          <div className="flex flex-col items-center gap-3">
            <div className="w-32 h-32 rounded-full overflow-hidden">
              <img
                src={Proud}
                alt="Chanya Nimtavorn"
                className="w-full h-full object-cover"
              />
            </div>
            <h3 className="text-lg font-semibold">Chanya Nimtavorn</h3>
            <p className="text-gray-300 text-sm">UX/UI Designer</p>
            <div className="flex gap-4 mt-2">
              <a
                href="https://github.com/CNProud"
                target="_blank"
                className="text-gray-300 hover:text-white"
              >
                <FontAwesomeIcon icon={faGithub} size="lg" />
              </a>
              <a
                href="https://www.instagram.com/pr_channi/"
                target="_blank"
                className="text-gray-300 hover:text-white"
              >
                <FontAwesomeIcon icon={faInstagram} size="lg" />
              </a>
            </div>
          </div>
        </div>
      </div>
      {/* Community Section */}
      <div className="h-screen flex flex-col justify-center items-center bg-[#0A3160] px-8 text-center">
        <h2 className="text-4xl font-bold mb-4">Join Our Community</h2>
        <p className="max-w-2xl text-lg">
          Join our community of learners and coders. Share your solutions,
          discuss challenges, and improve together. Be part of a growing network
          of passionate programmers.
        </p>
        <a
          href="/tasks"
          className="mt-6 px-6 py-3 bg-[#112538] hover:bg-[#17548B] rounded-lg font-semibold transition"
        >
          Start Coding
        </a>
      </div>
    </div>
  );
}

export default About;
