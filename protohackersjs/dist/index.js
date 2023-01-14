"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const net_1 = __importDefault(require("net"));
let server = net_1.default.createServer();
server.on('connection', (socket) => {
    socket.on('data', (data) => {
        socket.write(data);
    });
});
server.listen(4000);
