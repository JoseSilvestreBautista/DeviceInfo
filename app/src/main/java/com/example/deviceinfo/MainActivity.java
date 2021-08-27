package com.example.deviceinfo;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.deviceinfo.db_room.AppDatabase;
import com.example.deviceinfo.db_room.DeviceDb;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Drives the main program.
 *
 * @author Jose Silvestre-Bautista
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  /**
   * Holds the query parameters from the decoded version of the encoded URL.
   */
  private Map<String, String> parameters = null;
  /**
   * Holds the response from the post request.
   */
  private TextView apiResponseTextView;
  /**
   * Holds both device info and parameters which is used in the RecycleView.
   */
  private String[] dbList;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //Like variables
    apiResponseTextView = findViewById(R.id.api_response_textview);
    Button formatJsonButton = findViewById(R.id.Formatter_Button);
    formatJsonButton.setOnClickListener(this);
    String url = "https%3a%2f%2fm.alltheapps.org%2fget%2fapp%3fuserId%3dB1C92850-8202-44AC-B514-1849569F37B6%26implementationid%3dcl-and-erp%26trafficSource%3derp%26userClass%3d20200101";

    // methods called
    parameters = splitQuery(url);
    saveToDb();
    settingDbList();
    populateRecyclerView();

  }

  /**
   * Listens for the Format button to be pressed so it can call createJson() to create Json from
   * device info and query parameters from url.
   *
   * @param view Format button (in this case)
   */
  public void onClick(View view) {
    JSONObject postingJson;
    if (view.getId() == R.id.Formatter_Button) {
      postingJson = createJson();
      PostRequest(postingJson);

    }
  }

  /**
   * Decodes the encoded url and parses the url for query parameters.
   *
   * @param encodedURL The given encoded URL from the case study.
   * @return A map with query parameters.
   */
  public Map<String, String> splitQuery(String encodedURL) {
    Map<String, String> query_pairs = null;
    try {
      URL url = new URL(java.net.URLDecoder.decode(encodedURL, StandardCharsets.UTF_8.name()));

      query_pairs = new LinkedHashMap<String, String>();
      String query = url.getQuery();
      String[] pairs = query.split("&");
      for (String pair : pairs) {
        int idx = pair.indexOf("=");
        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return query_pairs;
  }

  /**
   * Stores query parameters and device info to a Room database.
   */
  private void saveToDb() {

    AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
    DeviceDb joseDevice = new DeviceDb();
    db.DatabaseDao().deleteAll();

    joseDevice.android_id = Settings.Secure
        .getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    joseDevice.manufacturer = Build.MANUFACTURER;
    joseDevice.model = Build.MODEL;
    joseDevice.user = Build.USER;
    joseDevice.product = Build.PRODUCT;
    joseDevice.android_version = VERSION.RELEASE;
    joseDevice.userId = parameters.get("userId");
    joseDevice.implementationid = parameters.get("implementationid");
    joseDevice.trafficSource = parameters.get("trafficSource");
    joseDevice.userClass = parameters.get("userClass");
    db.DatabaseDao().insertDevice(joseDevice);


  }

  /**
   * Stores query parameters and device info to dbList which is used for the RecycleView.
   */
  private void settingDbList() {
    dbList = new String[]{"Android ID: " + Settings.Secure
        .getString(getContentResolver(), Settings.Secure.ANDROID_ID),
        "Manufacturer: " + Build.MANUFACTURER, "Model: " + Build.MODEL, "User: " + Build.USER,
        "Product: " + Build.PRODUCT, "Android Version: " + VERSION.RELEASE,
        "Parameter: userId=" + parameters.get("userId"),
        "Parameter: implementationid=" + parameters.get("implementationid"),
        "Parameter: trafficSource=" + parameters.get("trafficSource"),
        "Parameter: userClass=" + parameters.get("userClass")};
  }

  /**
   * Sets up a RecycleView adapter and and sends the dbList to the DeviceDBListAdapter so we can
   * display query parameters and device info to the screen.
   */
  private void populateRecyclerView() {
    DeviceDbListAdapter deviceDbListAdapter = new DeviceDbListAdapter(this);
    AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
    deviceDbListAdapter.setDeviceDbList(dbList);

    RecyclerView recyclerView = findViewById(R.id.recycler_View);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
        DividerItemDecoration.VERTICAL);
    recyclerView.addItemDecoration(dividerItemDecoration);
    recyclerView.setAdapter(deviceDbListAdapter);
  }

  /**
   * Sends a post request to the Endpoint url given in the case study and receives a response that
   * is diplayed to the screen.
   *
   * @param postingJson JsonObject to be posted to endpoint url. It is all the created Json object
   *                    from createJson().
   */
  private void PostRequest(JSONObject postingJson) {

    String URL = "https://casestudy.alltheapps.org/mobile/install";
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    JsonObjectRequest objectRequest = new JsonObjectRequest(
        Request.Method.POST, URL, postingJson,
        apiResponse -> {
          Log.e("Rest Response", apiResponse.toString());
          apiResponseTextView.setText(apiResponse.toString());
        },
        error -> Log.e("Rest Response", error.toString())

    ) {
      protected Map<String, String> getParams() {

        return parameters;
      }

    };

    requestQueue.add(objectRequest);

  }

  /**
   * Creates a JsonObject that contains query parameters and device info and returns the created
   * Json object: schemaJson.
   *
   * @return schemaJson is returned which will be used in posting.
   */
  private JSONObject createJson() {
    JSONObject schemaJson = new JSONObject(), deviceInfoJson = new JSONObject(), parametersJson = new JSONObject();
    JSONArray deviceArray = new JSONArray(), parametersArray = new JSONArray();
    try {
      schemaJson.put("deviceInfo", deviceArray);
      deviceInfoJson.put("androidID",
          Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
      deviceInfoJson.put("manufacturer", Build.MANUFACTURER);
      deviceInfoJson.put("model", Build.MODEL);
      deviceInfoJson.put("user", Build.USER);
      deviceInfoJson.put("product", Build.PRODUCT);
      deviceInfoJson.put("androidVersion", VERSION.RELEASE);
      deviceArray.put(deviceInfoJson);
      schemaJson.put("parameters", parametersArray);
      parametersJson.put("userId", parameters.get("userId"));
      parametersJson.put("implementationid", parameters.get("implementationid"));
      parametersJson.put("trafficSource", parameters.get("trafficSource"));
      parametersJson.put("userClass", parameters.get("userClass"));
      parametersArray.put(parametersJson);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return schemaJson;

  }


}