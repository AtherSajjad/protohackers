import net, { Server, Socket } from "net";
import Ajv, { DefinedError, JSONSchemaType } from "ajv";
import { checkIsPrime } from "./utils/primeutils";
const ajv = new Ajv();

let server: Server = net.createServer();

const PORT: number = 4000;

const schema: JSONSchemaType<Input> = {
  type: "object",
  properties: {
    method: { type: "string", enum: ["isPrime"] },
    number: { type: "number" },
  },
  required: ["method", "number"],
  additionalProperties: true,
};

const validate = ajv.compile<Input>(schema);

interface Input {
  method: string;
  number: number;
}

interface Response {
  method: "isPrime",
  prime: boolean
}

server.on("connection", (socket) => {
  socket.on("data", (data: Buffer) => {
    if (!validateInput(data.toString())) {
      console.log("Validation failed\n");
      socket.end();
      return;
    }

    handleData(JSON.parse(data.toString()), socket);
  });
});

function validateInput(data: string) {
  try {
    let input = JSON.parse(data);
    return validate(input);
  }catch(err) {
    return false;
  }
}

function handleData(data: Input, socket: Socket) {
  let isPrime: boolean = checkIsPrime(data.number);
  let response: Response = {
    method: "isPrime",
    prime: isPrime
  }

  socket.write(JSON.stringify(response)+"\n");
}

server.on("listening", () => {
  console.log(`PrimeTime server started at ${PORT}`);
});


server.listen(PORT);
