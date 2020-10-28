package com.smartwebarts.developer.naujyu.retrofit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.Gson;
import com.smartwebarts.developer.naujyu.R;
import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.LoginData;
import com.smartwebarts.developer.naujyu.models.MessageModel;
import com.smartwebarts.developer.naujyu.models.SignUpModel;
import com.smartwebarts.developer.naujyu.models.StateModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.models.VendorDetails;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public enum UtilMethods {

    INSTANCE;

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void internetNotAvailableMessage(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog .setContentText("Internet Not Available");
        dialog.show();
    }

    public boolean isValidMobile(String mobile) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mobilePattern = "^(?:0091|\\\\+91|0)[7-9][0-9]{9}$";
        String mobileSecPattern = "[7-9][0-9]{9}$";

        if (mobile.matches(mobilePattern) || mobile.matches(mobileSecPattern)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidEmail(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void Login(final Context context, String mobile, String password , final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<LoginData> call = git.login( "1",mobile, password);
            call.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body().getId()!=null && !response.body().getId().isEmpty()) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }

    public void signup(final Context context, String fullname, String mobile, String password, String state, String city, String zipcode, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<SignUpModel> call = git.signup("1", fullname, mobile, password, state, city, zipcode);
            call.enqueue(new Callback<SignUpModel>() {
                @Override
                public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail(response.body().getMessage());
                       }
                   } else {
                       callBackResponse.fail("Invalid UserId or Password");
                   }
                }

                @Override
                public void onFailure(Call<SignUpModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void vehicletype(final Context context,final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<VehicleModel>> call = git.vehiclemodel("1");
            call.enqueue(new Callback<List<VehicleModel>>() {
                @Override
                public void onResponse(Call<List<VehicleModel>> call, Response<List<VehicleModel>> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().size()>0) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail("");
                       }
                   } else {
                       callBackResponse.fail("");
                   }
                }

                @Override
                public void onFailure(Call<List<VehicleModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void brand(final Context context,  String brand, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<BrandModel>> call = git.brand("1", brand);
            call.enqueue(new Callback<List<BrandModel>>() {
                @Override
                public void onResponse(Call<List<BrandModel>> call, Response<List<BrandModel>> response) {
                    dialog.dismiss();
                   String strResponse = new Gson().toJson(response.body());
                   Log.e("strResponse",strResponse);
                   if (response.body()!=null) {
                       if (response.body()!=null && response.body().size()>0) {
                           callBackResponse.success("", strResponse);
                       }
                       else {
                           callBackResponse.fail("");
                       }
                   } else {
                       callBackResponse.fail("");
                   }
                }

                @Override
                public void onFailure(Call<List<BrandModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void model(final Context context,  String brand, final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<BrandModel>> call = git.model( brand, "1");
            call.enqueue(new Callback<List<BrandModel>>() {
                @Override
                public void onResponse(Call<List<BrandModel>> call, Response<List<BrandModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().size()>0) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("");
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<List<BrandModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void booking(final Context context,
                        String customerid,
                        String vehiclenumber,
                        String vehicletype,
                        String vehiclemodel,
                        String fueltype,
                        String location,
                        String latitude,
                        String longitude,
                        String pincode,
                        String brandid,
                        String issue,
                        final mCallBackResponse callBackResponse) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<BookingResponse> call = git.booking(customerid,vehiclenumber,vehicletype,vehiclemodel,
                    fueltype,"1",issue, location,latitude,longitude,pincode,brandid);
            call.enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body()!=null && response.body().getMessage()!=null) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("");
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<BookingResponse> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }

    }


    public void state(Context context, mCallBackResponse callBackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<StateModel>> call = git.state("1");
            call.enqueue(new Callback<List<StateModel>>() {
                @Override
                public void onResponse(Call<List<StateModel>> call, Response<List<StateModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("");
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<List<StateModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void cities(Context context, String statename, mCallBackResponse callBackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<List<StateModel>> call = git.city("1", statename);
            call.enqueue(new Callback<List<StateModel>>() {
                @Override
                public void onResponse(Call<List<StateModel>> call, Response<List<StateModel>> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
                        if (response.body().size()>0) {
                            callBackResponse.success("", strResponse);
                        }
                        else {
                            callBackResponse.fail("");
                        }
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<List<StateModel>> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void loginDetails(Context context, String id, mCallBackResponse callBackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<VendorDetails> call = git.loginDetails("1", id);
            call.enqueue(new Callback<VendorDetails>() {
                @Override
                public void onResponse(Call<VendorDetails> call, Response<VendorDetails> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null) {
//                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<VendorDetails> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

    public void uploadProfile(Context context,String id, String name, String mobile, String password, String shopname, String location , String stateid, String cityid, String zipcode, String gstin, mCallBackResponse callBackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.default_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar progressBar = (ProgressBar)dialog.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        dialog.show();

        try {
            EndPointInterface git = APIClient.getClient().create(EndPointInterface.class);
            Call<MessageModel> call = git.updateProfile( "1", name, mobile, password, shopname, location, stateid, cityid, zipcode, gstin, id);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    dialog.dismiss();
                    String strResponse = new Gson().toJson(response.body());
                    Log.e("strResponse",strResponse);
                    if (response.body()!=null && response.body().getMessage()!=null && response.body().getMessage().equalsIgnoreCase("success")) {
                        callBackResponse.success("", strResponse);
                    } else {
                        callBackResponse.fail("");
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    callBackResponse.fail(t.getMessage());
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callBackResponse.fail(e.getMessage());
            dialog.dismiss();
        }
    }

}
