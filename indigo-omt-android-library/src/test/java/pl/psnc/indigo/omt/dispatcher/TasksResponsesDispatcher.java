package pl.psnc.indigo.omt.dispatcher;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * Created by michalu on 21.03.17.
 */

public class TasksResponsesDispatcher extends Dispatcher {
    @Override public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
        if (request.getPath().equals("/tasks?user=test&status=ANY")) {
            return new MockResponse().setBody("{\n"
                + "  \"tasks\": [\n"
                + "    {\n"
                + "      \"status\": \"WAITING\",\n"
                + "      \"description\": \"031d0cbe-ec96-4d29-9cbb-cc6d922bdde1\",\n"
                + "      \"user\": \"michalu-dev\",\n"
                + "      \"date\": \"2016-10-20T11:36:14Z\",\n"
                + "      \"id\": \"222\",\n"
                + "      \"output_files\": [\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.out\",\n"
                + "          \"name\": \"sayhello.out\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.err\",\n"
                + "          \"name\": \"sayhello.err\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"application\": \"2\",\n"
                + "      \"_links\": [\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/222\",\n"
                + "          \"rel\": \"self\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/222/input\",\n"
                + "          \"rel\": \"input\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"arguments\": [],\n"
                + "      \"input_files\": [\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.sh\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.txt\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"last_change\": \"2016-10-20T11:36:14Z\"\n"
                + "    },\n"
                + "    {\n"
                + "      \"status\": \"DONE\",\n"
                + "      \"description\": \"Simple task | testing\",\n"
                + "      \"user\": \"michalu-dev\",\n"
                + "      \"date\": \"2016-10-20T11:38:32Z\",\n"
                + "      \"id\": \"223\",\n"
                + "      \"output_files\": [\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.out\",\n"
                + "          \"name\": \"sayhello.out\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.err\",\n"
                + "          \"name\": \"sayhello.err\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"application\": \"2\",\n"
                + "      \"_links\": [\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223\",\n"
                + "          \"rel\": \"self\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223/input\",\n"
                + "          \"rel\": \"input\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"arguments\": [],\n"
                + "      \"input_files\": [\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.sh\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.txt\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"last_change\": \"2016-10-20T11:38:32Z\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n");
        }
        else if (request.getPath().equals("/tasks?user=test&status=DONE")) {
            return new MockResponse().setBody("{\n"
                + "  \"tasks\": [\n"
                + "    {\n"
                + "      \"status\": \"DONE\",\n"
                + "      \"description\": \"Simple task | testing\",\n"
                + "      \"user\": \"michalu-dev\",\n"
                + "      \"date\": \"2016-10-20T11:38:32Z\",\n"
                + "      \"id\": \"223\",\n"
                + "      \"output_files\": [\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.out\",\n"
                + "          \"name\": \"sayhello.out\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.err\",\n"
                + "          \"name\": \"sayhello.err\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"application\": \"2\",\n"
                + "      \"_links\": [\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223\",\n"
                + "          \"rel\": \"self\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223/input\",\n"
                + "          \"rel\": \"input\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"arguments\": [],\n"
                + "      \"input_files\": [\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.sh\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.txt\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"last_change\": \"2016-10-20T11:38:32Z\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n");
        }
        else if (request.getPath().equals("/tasks?user=test&status=DONE&application=2")) {
            return new MockResponse().setBody("{\n"
                + "  \"tasks\": [\n"
                + "    {\n"
                + "      \"status\": \"DONE\",\n"
                + "      \"description\": \"Simple task | testing\",\n"
                + "      \"user\": \"michalu-dev\",\n"
                + "      \"date\": \"2016-10-20T11:38:32Z\",\n"
                + "      \"id\": \"223\",\n"
                + "      \"output_files\": [\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.out\",\n"
                + "          \"name\": \"sayhello.out\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"url\": \"file?path=&name=sayhello.err\",\n"
                + "          \"name\": \"sayhello.err\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"application\": \"2\",\n"
                + "      \"_links\": [\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223\",\n"
                + "          \"rel\": \"self\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"href\": \"/v1.0/tasks/223/input\",\n"
                + "          \"rel\": \"input\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"arguments\": [],\n"
                + "      \"input_files\": [\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.sh\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"status\": \"READY\",\n"
                + "          \"name\": \"sayhello.txt\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"last_change\": \"2016-10-20T11:38:32Z\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n");
        }
        return new MockResponse().setResponseCode(404);
    }
}