package jonomoneta.juno.moviecash.Retrofit;

import java.io.File;

import jonomoneta.juno.moviecash.Model.Response.AllWinnerResponse;
import jonomoneta.juno.moviecash.Model.Response.AnswerCountResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyEndorseResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyGoldanBuzzerResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyLifeLineResponse;
import jonomoneta.juno.moviecash.Model.Response.ApplyWinnerRewardResponse;
import jonomoneta.juno.moviecash.Model.Response.ChargePurchaseDiamondsPaymentResponse;
import jonomoneta.juno.moviecash.Model.Response.ClaimPlanResponse;
import jonomoneta.juno.moviecash.Model.Response.CommentResponse;
import jonomoneta.juno.moviecash.Model.Response.CommonResponse;
import jonomoneta.juno.moviecash.Model.Response.CountryResponse;
import jonomoneta.juno.moviecash.Model.Response.GenerateLottoClubResultResponse;
import jonomoneta.juno.moviecash.Model.Response.GenerateQR;
import jonomoneta.juno.moviecash.Model.Response.GetAdvertisementResponse;
import jonomoneta.juno.moviecash.Model.Response.GetAllBestTalentBankVideosResponse;
import jonomoneta.juno.moviecash.Model.Response.GetAllTalentBankVideosByUserResponse;
import jonomoneta.juno.moviecash.Model.Response.GetCurrentLottoClubSelectedNoResponse;
import jonomoneta.juno.moviecash.Model.Response.GetGoogleSearchLocationResponse;
import jonomoneta.juno.moviecash.Model.Response.GetNextQuizGameDateTimeResponse;
import jonomoneta.juno.moviecash.Model.Response.GetQuizGamePaymentPlanResponse;
import jonomoneta.juno.moviecash.Model.Response.GetSearchLocationsTypesResponse;
import jonomoneta.juno.moviecash.Model.Response.GetTalentCategoryListResponse;
import jonomoneta.juno.moviecash.Model.Response.GetTheaterResponse;
import jonomoneta.juno.moviecash.Model.Response.GettLottoClubRewardPlansResponse;
import jonomoneta.juno.moviecash.Model.Response.LottoClubFreeSubscribeUserResponse;
import jonomoneta.juno.moviecash.Model.Response.LottoHistoryResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieTypesResponse;
import jonomoneta.juno.moviecash.Model.Response.OTPResponse;
import jonomoneta.juno.moviecash.Model.Response.MovieDetailsResponse;
import jonomoneta.juno.moviecash.Model.Response.PlaceResponse;
import jonomoneta.juno.moviecash.Model.Response.Prediction;
import jonomoneta.juno.moviecash.Model.Response.QuizGameFreeSubscribeUserResponse;
import jonomoneta.juno.moviecash.Model.Response.QuizPrizeListResponse;
import jonomoneta.juno.moviecash.Model.Response.QuizResponse;
import jonomoneta.juno.moviecash.Model.Response.RewardHistoryResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveAnswer;
import jonomoneta.juno.moviecash.Model.Response.SaveQuizAnswerResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveUserTokenResponse;
import jonomoneta.juno.moviecash.Model.Response.SaveVideoReviewResponse;
import jonomoneta.juno.moviecash.Model.Response.TrailerDetailsResponse;
import jonomoneta.juno.moviecash.Model.Response.UserDetailsResponse;
import jonomoneta.juno.moviecash.Model.Response.WatchTrailerResponse;
import jonomoneta.juno.moviecash.Model.Response.WinnerResponse;
import jonomoneta.juno.moviecash.Model.Response.WorldsBestSubscriptionResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface RetroInterface {

    @FormUrlEncoded
    @POST("SaveMobileOTP")
    Call<OTPResponse> sendOTP(@Field("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("ResendMobileOTP")
    Call<OTPResponse> reSendOTP(@Field("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("VerifyMobileOTP")
    Call<OTPResponse> verifyOTP(@Field("mobileno") String mobileno, @Field("otpcode") String otpcode);

    @GET("GetMoviesWithVideoByFilter")
    Call<MovieResponse> getMovieList(@Query("pagenumber") String pagenumber, @Query("status") String status,
                                     @Query("categoryid") String categoryid, @Query("typeids") String typeids,
                                     @Query("languageid") String languageid, @Query("releasedate") String releasedate,
                                     @Query("countryid") String countryid, @Query("searchtext") String searchtext);

    @GET("GetMovieTrailersByMovieID")
    Call<MovieDetailsResponse> getMovieDetails(@Query("movieid") int movieid);

    @GET("GetMovieTrailersByID")
    Call<TrailerDetailsResponse> getTrailerDetails(@Query("trailerid") int trailerid, @Query("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("SaveApplicationUser")
    Call<UserDetailsResponse> saveUserProfile(@Field("id") int id, @Field("name") String name,
                                              @Field("mobileno") String mobileno, @Field("birthdate") String birthdate,
                                              @Field("movietypeids") String movietypeids, @Field("refercode") String refercode,
                                              @Field("profilepicture") String profilepicture, @Field("gender") String gender);

    @GET("GetApplicationUserDetail")
    Call<UserDetailsResponse> getUserProfile(@Query("mobileno") String mobileno);

    @FormUrlEncoded
    @POST("SaveApplicationUserToken")
    Call<UserDetailsResponse> saveUserToken(@Field("mobileno") String mobileno, @Field("token") String token);

    @FormUrlEncoded
    @POST("SaveApplicationUserComment")
    Call<CommentResponse> saveUserComment(@Field("trailerid") int trailerid, @Field("movieid") int movieid,
                                          @Field("userid") int userid, @Field("comments") String comment);

    @GET("GetMovieTypesDropdown")
    Call<MovieTypesResponse> getMovieTypes();

    @GET("GetMovieCategoriesDropdown")
    Call<MovieTypesResponse> getMovieCategories();

    @GET("GetCountriesDropdown")
    Call<CountryResponse> getCountries();

    @GET("GetMovieLanguageDropdown")
    Call<MovieTypesResponse> getLanguage();

    @FormUrlEncoded
    @POST("WatchTrailer")
    Call<WatchTrailerResponse> watchTrailer(@Field("mobileno") String mobileno, @Field("totalearning") String totalearning);

    @FormUrlEncoded
    @POST("WatchMovies")
    Call<WatchTrailerResponse> watchMovies(@Field("mobileno") String mobileno, @Field("totalearning") String totalearning);

    @FormUrlEncoded
    @POST("WatchVideo")
    Call<WatchTrailerResponse> watchAdvertisment(@Field("mobileno") String mobileno, @Field("videoid") String videoid,
                                                 @Field("totalearning") String totalearning, @Field("earncount") String earncount);

    @GET("GetTheaterOrNotByLatLong")
    Call<GetTheaterResponse> getTheater(@Query("latitude") String latitude, @Query("longitude") String longitude, @Query("mobileno") String mobileno);

    @GET("GetPredictionsByUsers")
    Call<Prediction> getPredictionList(@Query("userid") int userid, @Query("countrycode") String countrycode);

    @FormUrlEncoded
    @POST("SavePredictionAnswer")
    Call<SaveAnswer> saveAnswer(@Field("userid") int userid, @Field("predictionquestionid") int predictionquestionid,
                                @Field("predictionanswerid") int predictionanswerid,
                                @Field("predictionanswertext") String predictionanswertext);

    @FormUrlEncoded
    @POST("GenerateUserQRCode")
    Call<GenerateQR> generateQrCode(@Field("userid") int userid, @Field("referCode") String referCode);

    @GET("json")
    Call<PlaceResponse> getPlaces(@Query("location") String location, @Query("radius") String radius,
                                  @Query("keyword") String keyword, @Query("type") String type, @Query("key") String key);

    @FormUrlEncoded
    @POST("SaveUserGPSTrackingHistory")
    Call<CommonResponse> saveHistory(@Field("userid") int userid, @Field("placename") String placename, @Field("placeid") String placeid,
                                     @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("address") String address,
                                     @Field("types") String types, @Field("hours") int hours, @Field("minutes") int minutes,
                                     @Field("seconds") int seconds);

    @GET("GetQuizGameByUsers")
    Call<QuizResponse> getQuizList(@Query("userid") int userid, @Query("countrycode") String countrycode, @Query("currentquizid") int currentquizid);

    @FormUrlEncoded
    @POST("SaveQuizGameAnswer")
    Call<SaveQuizAnswerResponse> saveQuizAnswer(@Field("userid") int userid, @Field("quizgameid") int quizgameid,
                                                @Field("quizgamequestionid") int quizgamequestionid,
                                                @Field("quizgamequestionanswerid") int quizgamequestionanswerid,
                                                @Field("gamelevel") int gamelevel, @Field("isright") boolean isright);

    @GET("GetQuizGameCompletedListByUsers")
    Call<QuizPrizeListResponse> getQuizPrizeList(@Query("userid") int userid);

    @FormUrlEncoded
    @POST("SaveQuizGameForClaim")
    Call<SaveQuizAnswerResponse> claimQuizPrize(@Field("userid") int userid, @Field("quizgameuserattendid") int quizgameuserattendid);

    @FormUrlEncoded
    @POST("ClaimMovieCashPrize")
    Call<SaveQuizAnswerResponse> redeemPrize(@Field("userid") int userid, @Field("mobileno") String mobileno,
                                             @Field("fullname") String fullname, @Field("email") String email,
                                             @Field("address") String address, @Field("claimtype") String claimtype);

    @GET("GetClaimPlans")
    Call<ClaimPlanResponse> getClaimPlan();

    @GET("GetQuizGameQuestionAnswerCount ")
    Call<AnswerCountResponse> getAnswerCount(@Query("quizgamequestionid") int quizgamequestionid);

    @GET("GetTopWinners ")
    Call<WinnerResponse> getTopWinnersList(@Query("userid") int userid, @Query("quizgameid") int quizgameid);

    @GET("GetQuizGameWinners ")
    Call<AllWinnerResponse> getQuizWinnersList(@Query("quizgameid") int quizgameid, @Query("pagenumber") int pagenumber);

    @GET("GetNextQuizGameDateTime ")
    Call<GetNextQuizGameDateTimeResponse> getNextQuizGameDateTime(@Query("countrycode") String countrycode);

    @FormUrlEncoded
    @POST("ApplyWinnerReward")
    Call<ApplyWinnerRewardResponse> applyWinnerReward(@Field("mobileno") String mobileno, @Field("totalearning") long totalearning,
                                                      @Field("userid") int userid, @Field("quizgameid") int quizgameid,
                                                      @Field("quizgametype") String QuizGameType);

    @FormUrlEncoded
    @POST("ApplyMegaQuizWinnerReward")
    Call<ApplyWinnerRewardResponse> applyMegaQuizWinnerReward(@Field("mobileno") String mobileno, @Field("totalearning") long totalearning,
                                                              @Field("userid") int userid, @Field("quizgameid") int quizgameid,
                                                              @Field("quizgametype") String QuizGameType);

    @FormUrlEncoded
    @POST("ChargePurchaseDiamondsPayment")
    Call<ChargePurchaseDiamondsPaymentResponse> purchaseDiamond(@Field("userid") int userid, @Field("stripecustomerid") String stripecustomerid,
                                                                @Field("amount") double amount, @Field("quantity") int quantity,
                                                                @Field("description") String description, @Field("email") String email,
                                                                @Field("cardname") String cardname, @Field("cardnumber") String cardnumber,
                                                                @Field("cardexpyear") int cardexpyear, @Field("cardexpmonth") int cardexpmonth,
                                                                @Field("cardcvc") String cardcvc);

    @FormUrlEncoded
    @POST("SubscribeUser")
    Call<ChargePurchaseDiamondsPaymentResponse> subscribeUser(@Field("userid") int userid, @Field("stripecustomerid") String stripecustomerid,
                                                              @Field("amount") double amount, @Field("description") String description,
                                                              @Field("email") String email, @Field("cardname") String cardname,
                                                              @Field("cardnumber") String cardnumber, @Field("cardexpyear") int cardexpyear,
                                                              @Field("cardexpmonth") int cardexpmonth, @Field("cardcvc") String cardcvc,
                                                              @Field("referralcode") String referralcode);


    @FormUrlEncoded
    @POST("ApplyLifeLine")
    Call<ApplyLifeLineResponse> applyLifeLine(@Field("userid") int userid, @Field("lifeline") int lifeline,
                                              @Field("gamelevel") int gamelevel, @Field("quizgameid") int quizgameid,
                                              @Field("quizgamequestionid") int quizgamequestionid,
                                              @Field("quizgamequestionrightanswerid") int quizgamequestionrightanswerid);

    @GET("GetSubscriptionPaymentPlan")
    Call<GetQuizGamePaymentPlanResponse> getQuizGamePaymentPlan();

    @FormUrlEncoded
    @POST("ApplyEndorseUser")
    Call<ApplyEndorseResponse> applyEndorse(@Field("userid") int userid, @Field("referralcode") String referralcode);

    @FormUrlEncoded
    @POST("ApplyGoldanBuzzer")
    Call<ApplyGoldanBuzzerResponse> applyGoldenBuzzer(@Field("userid") int userid);

    @FormUrlEncoded
    @POST("ClaimMegaWalletAmount")
    Call<CommonResponse> claimMegaWalletAmount(@Field("userid") int userid, @Field("mobileno") String mobileno,
                                               @Field("fullname") String fullname, @Field("email") String email,
                                               @Field("address") String address, @Field("claimamount") double claimamount);

    @FormUrlEncoded
    @POST("WorldsBestSubscribeUser")
    Call<WorldsBestSubscriptionResponse> subscribeUserForWorldsBest(@Field("userid") int userid, @Field("stripecustomerid") String stripecustomerid,
                                                                    @Field("amount") double amount, @Field("description") String description,
                                                                    @Field("email") String email, @Field("cardname") String cardname,
                                                                    @Field("cardnumber") String cardnumber, @Field("cardexpyear") int cardexpyear,
                                                                    @Field("cardexpmonth") int cardexpmonth, @Field("cardcvc") String cardcvc);

    @FormUrlEncoded
    @POST("RegisterTalentBank")
    Call<CommonResponse> uploadWorldsBestForm(@Field("userid") int userid, @Field("originalfilename") String originalfilename,
                                              @Field("form64string") String form64string);

    @Multipart
    @POST("UploadTalentBankVideo")
    Call<CommonResponse> uploadWorldsBestTalentVideo(@Part("userid") RequestBody userid, @Part("originalfilename") RequestBody description,
                                                     @Part("base64string") RequestBody base64string,
                                                     @Part("categoryid") RequestBody categoryid, @Part MultipartBody.Part file);

    @GET("GetAllTalentBankVideos ")
    Call<GetAllTalentBankVideosByUserResponse> getAllTalentBankVideo(@Query("userid") int userid, @Query("pagenumber") int pagenumber,
                                                                     @Query("pagesize") int pagesize);

    @GET("GetAllTalentBankVideosByUser ")
    Call<GetAllTalentBankVideosByUserResponse> getTalentBankVideoByUser(@Query("userid") int userid, @Query("pagenumber") int pagenumber,
                                                                        @Query("pagesize") int pagesize, @Query("isreviewvideo") boolean isreviewvideo);

    @FormUrlEncoded
    @POST("SaveTalentBankVideosReview")
    Call<SaveVideoReviewResponse> saveVideoReview(@Field("videoid") int videoid, @Field("reviewuserid") int reviewuserid,
                                                  @Field("isreview") boolean isreview);

    @FormUrlEncoded
    @POST("RemoveTalentBankVideo")
    Call<CommonResponse> removeVideo(@Field("videoid") int videoid);

    @GET("GetWorldsBestVideoCategory ")
    Call<GetTalentCategoryListResponse> getTalentCategoryList();

    @GET("GetAllBestTalentBankVideos ")
    Call<GetAllBestTalentBankVideosResponse> getTrendingVideos(@Query("userid") int userid, @Query("pagenumber") int pagenumber,
                                                               @Query("pagesize") int pagesize);

    @FormUrlEncoded
    @POST("WorldsBestSubscribeUserForJudge")
    Call<WorldsBestSubscriptionResponse> subscribeForWorldsBestJudge(@Field("userid") int userid, @Field("stripecustomerid") String stripecustomerid,
                                                                     @Field("amount") double amount, @Field("description") String description,
                                                                     @Field("email") String email, @Field("cardname") String cardname,
                                                                     @Field("cardnumber") String cardnumber, @Field("cardexpyear") int cardexpyear,
                                                                     @Field("cardexpmonth") int cardexpmonth, @Field("cardcvc") String cardcvc);

    @FormUrlEncoded
    @POST("SaveLottoClubSelectedNo")
    Call<CommonResponse> saveLottoClubSelectedNo(@Field("userid") int userid, @Field("no1") String no1,
                                                 @Field("no2") String no2, @Field("no3") String no3,
                                                 @Field("no4") String no4, @Field("no5") String no5,
                                                 @Field("luckyno") String luckyno);


    @GET("GettLottoClubRewardPlans ")
    Call<GettLottoClubRewardPlansResponse> gettLottoClubRewardPlans();

    @GET("GetCurrentLottoClubSelectedNo ")
    Call<GetCurrentLottoClubSelectedNoResponse> getCurrentLottoClubSelectedNo(@Query("userid") int userid);

    @FormUrlEncoded
    @POST("LottoClubSubscribeUser")
    Call<WorldsBestSubscriptionResponse> lottoClubSubscribeUser(@Field("userid") int userid, @Field("stripecustomerid") String stripecustomerid,
                                                                @Field("amount") double amount, @Field("description") String description,
                                                                @Field("email") String email, @Field("cardname") String cardname,
                                                                @Field("cardnumber") String cardnumber, @Field("cardexpyear") int cardexpyear,
                                                                @Field("cardexpmonth") int cardexpmonth, @Field("cardcvc") String cardcvc);

    @GET("GettLottoClubSelectedNoListByUser ")
    Call<LottoHistoryResponse> gettLottoClubSelectedNoListByUser(@Query("userid") int userid, @Query("pagenumber") int pagenumber,
                                                                 @Query("pagesize") int pagesize);

    @FormUrlEncoded
    @POST("GenerateLottoClubResult")
    Call<GenerateLottoClubResultResponse> generateLottoClubResult(@Field("userid") int userid);

    @FormUrlEncoded
    @POST("LottoClubFreeSubscribeUser")
    Call<LottoClubFreeSubscribeUserResponse> lottoClubFreeSubscribeUser(@Field("userid") int userid);

    @FormUrlEncoded
    @POST("QuizGameFreeSubscribeUser")
    Call<QuizGameFreeSubscribeUserResponse> quizGameFreeSubscribeUser(@Field("userid") int userid);

    @GET("GetRewardHistoryByUser")
    Call<RewardHistoryResponse> getRewardHistory(@Query("userid") int userid);

    @GET("GetAdvertisement")
    Call<GetAdvertisementResponse> getAdvertisement(@Query("totalrecord") int totalrecord);

    @GET("GetSearchLocationsTypes")
    Call<GetSearchLocationsTypesResponse> getSearchLocationsTypes();

    @FormUrlEncoded
    @POST("SaveGoogleSearchLocation")
    Call<CommonResponse> saveGoogleSearchLocation(@Field("searchtype") String searchtype,
                                                  @Field(value = "searchlocationsjson", encoded = true) String searchlocationsjson);

    @GET("GetGoogleSearchLocation")
    Call<GetGoogleSearchLocationResponse> getGoogleSearchLocation(@Query("userid") int userid,
                                                                  @Query("searchtype") String searchtype,
                                                                  @Query("latitude") String latitude,
                                                                  @Query("longitude") String longitude);

    @FormUrlEncoded
    @POST("SaveRewardLocation")
    Call<CommonResponse> saveRewardLocation(@Field("userid") int userid, @Field("placeid") String placeid,
                                            @Field("reward") int reward);
}


