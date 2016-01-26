package search;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;










import com.netdania.swsapi.SwsAPI;
import com.netdania.swsapi.SwsFields;
import com.netdania.swsapi.SwsListener;
import com.netdania.swsapi.responses.AddAlertResponse;
import com.netdania.swsapi.responses.AffectedAlertsResponse;
import com.netdania.swsapi.responses.ChartUpdateResponse;
import com.netdania.swsapi.responses.ConfirmationResponse;
import com.netdania.swsapi.responses.EditDeleteTriggerAlertResponse;
import com.netdania.swsapi.responses.ErrorResponse; 
import com.netdania.swsapi.responses.GetAlertUserInformationResponse;
import com.netdania.swsapi.responses.GetDeletedAlertsResponse;
import com.netdania.swsapi.responses.GetSentAlertMessagesResponse;
import com.netdania.swsapi.responses.GetSingleAlertResponse;
import com.netdania.swsapi.responses.GetTriggeredAlertsResponse;
import com.netdania.swsapi.responses.GetUserGroupUsersResponse;
import com.netdania.swsapi.responses.InformationResponse;
import com.netdania.swsapi.responses.LookupResponse;
import com.netdania.swsapi.responses.MapObjectResponse;
import com.netdania.swsapi.responses.MonitorChartResponse;
import com.netdania.swsapi.responses.MonitorNewsResponse;
import com.netdania.swsapi.responses.MonitorPriceResponse;
import com.netdania.swsapi.responses.NewsHistoryResponse;
import com.netdania.swsapi.responses.NewsSearchResponse;
import com.netdania.swsapi.responses.NewsStoryResponse;
import com.netdania.swsapi.responses.ServerTriggeredNewsAlertResponse;
import com.netdania.swsapi.responses.ServerTriggeredPriceAlertResponse;
import com.netdania.swsapi.responses.ServerUserActivityResponse;

/**
 * This examples connects to a StreamingServer, and monitors quote data from on
 * two instruments. These instruments have the symbols "EURUSD" and "EURJPY".
 */
public class GBPUSD extends Thread implements SwsListener {
	// Change the below connection parameters to match your settings
	String group = "btxtraders";
	String password = "s12w6s88ip54a";
	String host = "http://balancer.netdania.com/StreamingServer/StreamingServer";
	String providerID = "netdania_fxa";
	String gbpusdTimeStamp="";
	private static LinkedList<String> yvalue=new LinkedList<String>();
	public static LinkedList<String> getYvalue() {
		return yvalue;
	}
	public void setYvalue(LinkedList<String> yvalue) {
		this.yvalue = yvalue;
	}
	private static LinkedList<Long> datelist=new LinkedList<Long>();

	public static LinkedList<Long> getDatelist() {
		return datelist;
	}
	public static void setDatelist(LinkedList<Long> datelist) {
		GBPUSD.datelist = datelist;
	}
	// SwsAPI is used to communicate with the StreamingServer
	private static SwsAPI m_api;

	private int reqID_GBPUSD;
	private String symbol="GBPUSD";
	private static String value="";
	
	public static String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void run() {
		// Instantiate a new SwsAPI object
		m_api = new SwsAPI(this);
		m_api.disableAutoFlush();
		m_api.selectProtocol(SwsAPI.STREAMING_PROTOCOL);
		m_api.setDefaultDataProvider(providerID);
		m_api.setForceStreaming(true);

		m_api.setConnectParams(group, null, password, host);

		System.out.println("Requesting Prices...");
		reqID_GBPUSD = m_api.monitorPrice(symbol);

		m_api.flush();
		System.out.println("Finished Requesting.");
	}
	static GBPUSD q;

	public static void startThread() {
		
		q=new GBPUSD();
		q.start();
	}
	public static void stopThread() {
		q.stop();
		m_api.disconnect();
		
	}
	long lastTimestamp=0;
	Calendar c1 = Calendar.getInstance();
	long lastDate=0,currentDate=0,temp=0;
	long diff=0;
	int j=0;
	String lastValue="";

	/* a price update has occured */
	public void swsPriceUpdated(MonitorPriceResponse resObject) {
		// Get request id, which identifies the request made for getting this data
		int reqID = resObject.getReqID();
		String last = resObject.get(SwsFields.QUOTE_LAST);
		// If last is "N/A" then this is maybe forex data, and then we just use bid instead
		if (last.equals("N/A"))
			last = resObject.get(SwsFields.QUOTE_BID);
		// Get time stamp 
		String strTimeStamp = resObject.get(SwsFields.QUOTE_TIME_STAMP);
		// If the time stamp is "N/A", then we will not attempt to convert it
		// into a java.util.Date object.
		if (!strTimeStamp.equals("N/A")) {
			long timeStamp = Double.valueOf(strTimeStamp).longValue() * 1000;
			java.util.Date date = new java.util.Date(timeStamp);
			strTimeStamp = date.toString();
		}
		// Get instrument's name
		String name = resObject.get(SwsFields.QUOTE_NAME);
		System.out.println("Price Updated: " + last + " , Time: " + strTimeStamp + ", Name: " + name);
		
		if(!gbpusdTimeStamp.equals(strTimeStamp))
		{
			System.out.println("Size = "+yvalue.size());
			setValue(last);
			gbpusdTimeStamp=strTimeStamp;
				try {
					c1.setTime(new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").parse(strTimeStamp));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				currentDate=c1.getTimeInMillis();
			
				if(yvalue.size()<300)
				{
						if(yvalue.size()==0)
						{
							yvalue.add(last);
							datelist.add(currentDate);
							System.out.println("Entered 0 && timestamp="+lastDate);
						}
						else
						{
							lastDate=datelist.getLast();
							lastValue=yvalue.getLast();
							diff=((currentDate/1000)-(lastDate/1000));
							for(j=1;j<=diff;j++)
							{
								if(j==diff)
								{
									datelist.add(currentDate);
									yvalue.add(last);
								}
								else
								{
									lastDate=lastDate/1000;
									lastDate++;
									temp=lastDate*1000;
									lastDate=temp;
									yvalue.add(lastValue);
									datelist.add(temp);
								}
							}
						}
					}
				else
				{
					System.out.println("Size = "+yvalue.size());
					lastDate=datelist.getLast();
					lastValue=yvalue.getLast();
					diff=((currentDate/1000)-(lastDate/1000));
					for(j=1;j<=diff;j++)
					{
						if(j==diff)
						{
							datelist.removeFirst();
							yvalue.removeFirst();
							
							datelist.add(currentDate);
							yvalue.add(last);
						}
						else
						{
							lastDate=lastDate/1000;
							lastDate++;
							temp=lastDate*1000;
							lastDate=temp;
							datelist.removeFirst();
							yvalue.removeFirst();
							yvalue.add(lastValue);
							datelist.add(temp);
						}
					}
				}
				setYvalue(yvalue);
		}
		// You can get the IDs of the fields modified since the last update
		// by calling the below method:
		short[] modifiedFieldIDs = resObject.getModifiedFIDs();
		// For all available fields, call:
		short[] availableFieldIDs = resObject.getAvailableFIDs();
	}

	/* an error has occured, and the communication cannot proceed */
	public void swsErrorOccured(ErrorResponse resObject) {
		if (resObject.getErrorCode() == ErrorResponse.ERROR_LOGIN) {
			// We are using incorrect connection parameters
		}
		System.out.println("Error Occured: " + resObject.getErrorMsg());
	}

	/* request is not available or not supported */
	public void swsUnavailableRequest(int reqID) {
		
		 if (reqID == reqID_GBPUSD)
			System.out.println("No data available for EURUSD");
	}

	/* information message */
	public void swsInformation(InformationResponse resObject) {
		if (resObject.getInfoCode() == InformationResponse.INFORMATION_DOING_RECONNECT) {
			// SwsAPI has lost the connection to the StreamingServer,
			// and is now attempting to reconnect.
		} else if (resObject.getInfoCode() == InformationResponse.INFORMATION_RECONNECTED) {
			// SwsAPI has now successfully reconnected to the StreamingServer.
			// Before you will get here, this event will have been triggered once before 
			// with a message of the type: INFORMATION_DOING_RECONNECT.
		}
		System.out.println("Information Message: " + resObject.getInfoMsg());
	}

	/* requested instrument lookup available */
	public void swsInstrumentLookup(LookupResponse resObject) {

	}

	/* requested historical data is available */
	public void swsHistoricalData(MonitorChartResponse resObject) {

	}

	/* an update is received for a monitored time scale */
	public void swsChartUpdated(ChartUpdateResponse resObject) {

	}

	/* requested historical headlines are available */
	public void swsHistoricalHeadlines(NewsHistoryResponse resObject) {

	}

	/* a new headline has been received */
	public void swsHeadlineUpdate(MonitorNewsResponse resObject) {

	}

	/* a news story has been received */
	public void swsNewsStory(NewsStoryResponse resObject) {

	}

	public void swsNewsSearch(NewsSearchResponse arg0) {

	}

	public void swsUnavailableRequest(int reqID, byte errorCode) {
		// TODO Auto-generated method stub

	}

	public void swsAddAlertResponse(AddAlertResponse addAlertResponse) {
		// TODO Auto-generated method stub

	}

	public void swsEditDeleteTriggerAlertResponse(EditDeleteTriggerAlertResponse editDeleteTriggerAlertResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetDeletedAlertsResponse(GetDeletedAlertsResponse getDeletedAlertsResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetTriggeredAlertsResponse(GetTriggeredAlertsResponse getTriggeredAlertsResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetUserGroupUsersResponse(GetUserGroupUsersResponse getUserGroupUsersResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetSentAlertMessagesResponse(GetSentAlertMessagesResponse getSentAlertMessagesResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetAlertUserInformation(GetAlertUserInformationResponse getAlertUserInformationResponse) {
		// TODO Auto-generated method stub

	}

	public void swsServerTriggeredNewsAlertResponse(ServerTriggeredNewsAlertResponse serverTriggeredNewsAlertResponse) {
		// TODO Auto-generated method stub

	}

	public void swsServerTriggeredPriceAlertResponse(ServerTriggeredPriceAlertResponse serverTriggeredPriceAlertResponse) {
		// TODO Auto-generated method stub

	}

	public void swsServerUserActivityResponse(ServerUserActivityResponse serverUserActivityResponse) {
		// TODO Auto-generated method stub

	}

	public void swsGetSingleAlertResponse(GetSingleAlertResponse getSingleAlertResponse) {
		// TODO Auto-generated method stub

	}

	public void swsAffectedAlertsResponse(AffectedAlertsResponse affectedAlertsResponse) {
		// TODO Auto-generated method stub

	}

	public void swsConfirmationResponse(ConfirmationResponse confirmationResponse) {
		// TODO Auto-generated method stub

	}

	public void swsMapObjectResponse(MapObjectResponse response) {
		// TODO Auto-generated method stub

	}

	
	
}
