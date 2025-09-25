function About() {
  return (
    <>
      <div className="h-screen w-screen text-white ">
        <div className="h-1/4 bg-[#0A3160] w-full flex flex-col justify-center items-center p-8">
          <div className="text-3xl">About Us</div>
          <div className="text-center">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nesciunt
            consectetur labore possimus accusantium hic obcaecati, provident
            voluptatibus ipsum autem iste laudantium? Dicta, velit nemo!
            Provident!
          </div>
        </div>
        <div className="h-1/4 bg-[#021526] w-full flex gap-2 items-center p-8">
          <div className="flex flex-col">
            <div className="text-2xl">Our Mission</div>
            <div className="w-1/2">
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nesciunt
              consectetur labore possimus accusantium hic obcaecati, provident
              voluptatibus ipsum autem iste laudantium? Dicta, velit nemo!
              Provident!
            </div>
          </div>
          <div className="w-1/2">img</div>
        </div>
        <div className="h-1/4 bg-[#112538] w-full flex flex-col justify-center items-center">
          <div className="text-2xl">Meeting Our Team</div>
          <div className="flex gap-2">
            <div>AAA</div>
            <div>BBB</div>
          </div>
        </div>
        <div className="h-1/4 bg-[#0A3160] w-full flex flex-col  justify-center items-center p-8">
          <div className=" text-3xl">Join Our Community</div>
          <div className="text-center">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Nam ad
            beatae velit nobis labore voluptatibus ipsum id explicabo quidem
            amet! Aspernatur cumque expedita earum vel id modi veritatis sequi
            dicta?
          </div>
        </div>
      </div>
    </>
  );
}

export default About;
