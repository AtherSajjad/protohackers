import net, { Server } from "net";

let server: Server = net.createServer();

const PORT: number = 4000;

server.on('connection', (socket)=> {
    socket.on('data', (data)=> {
        socket.write(data);
    })
});

server.on('listening', ()=> {
    console.log(`Echo server started at ${PORT}`);
})

server.listen(PORT);
