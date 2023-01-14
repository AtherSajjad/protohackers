import net, { Server } from "net";

let server: Server = net.createServer();

server.on('connection', (socket)=> {
    socket.on('data', (data)=> {
        socket.write(data);
    })
})

server.listen(4000);
