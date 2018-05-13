/*CopyrightPortions of this software are Copyright (c) 1993 - 2001, Chad Z. Hower (Kudzu) and the Indy Pit Crew - http://www.nevrona.com/Indy/LicenseRedistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:*	Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. *	Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation, about box and/or other materials provided with the distribution. *	No personal names or organizations names associated with the Indy project may be used to endorse or promote products derived from this software without specific prior written permission of the specific individual or organization. THIS SOFTWARE IS PROVIDED BY Chad Z. Hower (Kudzu) and the Indy Pit Crew "AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. Translation to java and modifications by Elifarley C. Coelho*/package elifarley.net;import elifarley.io.*;import elifarley.util.VersaThread;import java.net.*;import java.util.Vector;import java.io.IOException;public abstract class TCPServer /*extends JComponent*/ implements Messages {    protected int acceptWait = 1000;    protected boolean active, implicitThreadMgrCreated;    //protected TIdThreadMgr threadMgr;    public /*SocketHandles*/ ServerSocket bindings;    protected ListenerThread listenerThread;    public int terminateWaitTime = 5000;    public Class threadClass;    protected Vector threads;    protected int port;        // Occurs in the context of the peer thread    public ServerThreadEvent onExecute, onConnect, onDisconnect;        //protected ServerIntercept intercept;	public TCPServer(Object aOwner) {		super();			threads = new Vector();		//bindings = TIdSocketHandles.Create(Self);		//threadClass = PeerThread;		//  fSessionTimer = TTimer.Create(self);	}	public void finalize() throws Throwable{  		setActive(false);  		terminateAllThreads();  		bindings = null;  		threads = null;		super.finalize();	}    //property ThreadMgr: TIdThreadMgr read FThreadMgr write SetThreadMgr;    public boolean getImplicitThreadMgrCreated() {    	return implicitThreadMgrCreated;    }        public Vector getThreads() {    	return threads;    }	public int getAcceptWait() {		return acceptWait;	}		public void setAcceptWait(int value) throws Exception {  		if (getActive() ) {    		throw new Exception(RSAcceptWaitCannotBeModifiedWhileServerIsActive);  		}   		acceptWait = value;		//default 1000;    } 		public int getPort() {		return port;	}	public void setPort(int value) throws Exception {  		if (getActive() ) {    		throw new Exception("RSPortCannotBeModifiedWhileServerIsActive");  		}   		port = value;    } 	public boolean getActive() { 		return active; 	} 		/*	public int getDefaultPort() {		return defaultPort;	}		public void setDefaultPort(int value) {  		if (getActive() ) {    		throw new Exception(RSAcceptWaitCannotBeModifiedWhileServerIsActive);  		}   		defaultPort = value;		//default 1000;    }    */    	public void setActive(boolean aValue) throws IOException, InterruptedException {  		int i;  		if ( active != aValue /* && (! (csDesigning in ComponentState)) && (! (csLoading in ComponentState)) */ ) {    		if (aValue) {      			// Set up bindings      			/*      			if (bindings.count() < 1) {        			if (defaultPort > 0) {          				bindings.add();        			} else {          				throw new Exception(RSNobindingsSpecified);        			}				}				*/				      			bindings = new ServerSocket(getPort(), 50, null);								/*      			for (i = 0; i < bindings.count(); i++) {			        			        //bindings[i].allocateSocket();			        //bindings[i].bind();			        //bindings[i].listen();			        			        //bindings.elementAt(i) = new ServerSocket(port, backlog, null);      			}				*/								// Set up ThreadMgr				/*				implicitThreadMgrCreated =  threadMgr != null;				if (implicitThreadMgrCreated ) {					threadMgr = new ThreadMgrDefault(this);				}				threadMgr.threadClass = threadClass;				*/				      			bindings.setSoTimeout(acceptWait);				// Set up listener thread				listenerThread = new ListenerThread(this);				listenerThread.stopMode = VersaThread.SM_TERMINATE;				listenerThread.acceptWait = acceptWait;				listenerThread.v_start();			} else { //if (aValue) 				// Stop listening				/*				for (i = 0; i < bindings.count(); i++) {					bindings[i].closeSocket();				}				*/				bindings.close();				// Tear down ThreadMgr				terminateAllThreads();				if (implicitThreadMgrCreated) {					//threadMgr = null;				}				implicitThreadMgrCreated = false;				// Tear down Listener thread				listenerThread.terminateAndWaitFor();				listenerThread = null;			}  		}  		active = aValue;	}	public void doConnect(PeerThread axThread) {  		if (onConnect != null) {    		onConnect.act(axThread);  		}	}	public boolean doExecute(PeerThread aThread) {  		boolean result = onExecute != null;  		if (result) {    		onExecute.act(aThread);  		}  		return result;  	}	public void doDisconnect(PeerThread axThread) {  		if (onDisconnect != null) {    		onDisconnect.act(axThread);  		}	}	public void terminateAllThreads() {  		int i;  		Vector aList;  		PeerThread pThread;		final int lSleepTime = 250;  		aList = threads;  		  		synchronized(aList) {	  		try {	    		for (i = 0; i < aList.size(); i++) {	      			pThread = (PeerThread)(aList.elementAt(i));	      			pThread.getConnection().disconnectSocket();	      		}	    	} catch (Exception e) {}				}    	    	//threads.unlockList();  		// Must wait for all threads to terminate, as they access the server and bindings. if (this		// routine is being called from the destructor, this can cause AVs/		//		// This method is used instead of:		//  -Threads.WaitFor. Since they are being destroyed thread.Wait for could AV. And Waiting for		//   Handle produces different code for different OSs, and using common code has troubles		//   as the handles are quite different.		//  -Last thread signaling  		for (i = 1; i < (terminateWaitTime / lSleepTime); i++) {    		try {    			Thread.sleep(lSleepTime);    		} catch (InterruptedException ie) {    		}    		    		aList = threads;    		synchronized (aList) {	    		try {	      			if (aList.size() == 0) {	        			break;	        		}	      		} catch (Exception e) {}			}			    	}    }}/*	// uses IdSocketHandle, IdTCPConnection, IdThread, IdThreadMgr, IdThreadMgrDefault, IdIntercept;  // uses IdException, IdGlobal, IdResourceStrings, IdStack, IdStackConsts;function TIdTCPServer.GetDefaultPort: integer;{  result = bindings.DefaultPort;}void TIdTCPServer.Loaded;{  inherited;  // Active=True must not be performed before all other props are loaded  if (Active {    FActive = False;    Active = True;  }}void TIdTCPServer.Notification(AComponent: TComponent; Operation: TOperation);{  inherited;  // remove the reference to the linked components if (they are deleted  if ((Operation = opRemove) {    if ((AComponent = FThreadMgr) {      FThreadMgr = nil;    end else if ((AComponent = FIntercept) {      FIntercept = nil;    }  }}void TIdTCPServer.Setbindings(const abValue: TIdSocketHandles);{	bindings.Assign(abValue);}void TIdTCPServer.SetDefaultPort(const AValue: integer);{  bindings.DefaultPort = AValue;}void TIdTCPServer.SetIntercept(const Value: TIdServerIntercept);{  FIntercept = Value;  // Add self to the intercept's notification list  if (assigned(FIntercept) then  {    FIntercept.FreeNotification(Self);  }}void TIdTCPServer.SetThreadMgr(const Value: TIdThreadMgr);{  FThreadMgr = Value;  // Ensure we will be notified when the component is freed, even is it's on  // another form  if (Value <> nil {    Value.FreeNotification(self);  }}*/