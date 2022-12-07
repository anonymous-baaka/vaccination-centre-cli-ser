import java.util.ArrayList;

public class MyRequestQueue {
    int len;
    int ptr;
    public ArrayList<Request> arr=new ArrayList<Request>();
    MyRequestQueue()
    {
        //arr=null;
        len=0;
        ptr=-1;
    }
    ArrayList<Request>getArray()
    {
        ArrayList<Request>tmp=new ArrayList<Request>();
        for(Request r:arr)
            tmp.add(r);
        return tmp;
    }
    void push(Request request)
    {
        System.out.println("PUSHED: "+request.clientID+" "+request.clientIp+" "+request.timestamp);
        arr.add(request);
        //arr.
        ptr++;
        len++;
    }
    Request pop()
    {
        Request req=null;
        if(ptr<=0)
        {
            req=arr.remove(arr.size()-1);
            ptr--;
            len--;
        }
        return req;
        
    }
    Boolean pop(Request request)
    {
        for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).clientID==request.clientID && arr.get(i).timestamp==request.timestamp && arr.get(i).clientIp==request.clientIp)
            {
                //arr.remove(i);
                arr.get(i).status=Request.Status.COMPLETED;
                return true;
            }
        }
        return false;
    }

    Boolean update(Request request,char ch)
    {

       for(int i=0;i<arr.size();i++)
        {
            if(arr.get(i).clientID==request.clientID && arr.get(i).timestamp==request.timestamp && arr.get(i).clientIp==request.clientIp)
            {
                //arr.remove(i);
                if(ch=='W')
                    arr.get(i).status=Request.Status.WAITING;
                else if(ch=='P')
                    arr.get(i).status=Request.Status.PROCESSING;
                else
                    arr.get(i).status=Request.Status.COMPLETED;

                return true;
            }
        }
        return false;
    }

    void display()
    {
        for(Request req: arr)
        {
            System.out.println(req.clientID+"\t"+req.clientIp+"\t"+req.timestamp);
        }
    }
    int size()
    {
        return arr.size();
    }
    /*public static void main(String args[])
    {
        MyRequestQueue myRequestQueue=new MyRequestQueue();
        myRequestQueue.push(new Request(0, null));
        myRequestQueue.push(new Request(10, null));
        myRequestQueue.push(new Request(20, null));
        myRequestQueue.display();
    }*/
}
