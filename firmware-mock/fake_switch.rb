#! /home/mario/.rvm/rubies/default/bin/ruby

require "webrick"
port = Integer(ARGV[0]);

class MyServlet < WEBrick::HTTPServlet::AbstractServlet
	SWITCH_STATES = { false => 'off' , true => 'on'}
	@@switch_state = false;
	HTML_MESS = "{\"status\":";
	def do_GET (request, response)
		WEBrick::HTTPAuth.basic_auth(request, response, "My Realm") {|user, pass|
	        user == 'TheBestTeam' && pass == 'WiesioKiller'}

		case request.path
		when "/on"
			@@switch_state = true;
		when "/off"
			@@switch_state = false;
		end
		response.body=HTML_MESS + " \"" + SWITCH_STATES[@@switch_state] + "\"}"
		response.status = 200
	end
end

server = WEBrick::HTTPServer.new(:Port => port)

server.mount "/", MyServlet

trap("INT") {
    server.shutdown
}

server.start
