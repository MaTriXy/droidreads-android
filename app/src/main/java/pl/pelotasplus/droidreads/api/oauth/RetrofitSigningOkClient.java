/*
 * Copyright (C) 2013 Patrik Åkerfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.pelotasplus.droidreads.api.oauth;

import java.io.IOException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

public class RetrofitSigningOkClient extends OkClient {
    private final RetrofitHttpOAuthConsumer mOAuthConsumer;

    public RetrofitSigningOkClient(RetrofitHttpOAuthConsumer consumer) {
        mOAuthConsumer = consumer;
    }

//    public RetrofitSigningOkClient(OkHttpClient client, RetrofitHttpOAuthConsumer consumer) {
//        super(client);
//        mOAuthConsumer = consumer;
//    }

    @Override
    public Response execute(Request request) {
        Request requestToSend = request;

        try {
            RetrofitHttpRequestAdapter signedAdapter = (RetrofitHttpRequestAdapter) mOAuthConsumer.sign(request);
            requestToSend = (Request) signedAdapter.unwrap();
            return super.execute(requestToSend);
        } catch (OAuthMessageSignerException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            // Fail to sign, ignore
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
