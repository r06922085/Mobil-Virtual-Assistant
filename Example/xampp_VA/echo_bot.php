<?php
header("Content-Type:text/html; charset=utf-8");
/**
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

require_once('./LINEBotTiny.php');

$channelAccessToken = 'MdvKITEF3Wk8LgHzPCsHopcts17AfQ9KaP3bEOpdXv37+Z8124GhhuDRYgkLPfahkfaWN2itZNwHFOT0exi4o/qyX9GczLCWNKVURHMtvZHQXBE9HUyHF+kuyCGYfG5MCQBdxH8hULTAzzaUTuyvzAdB04t89/1O/w1cDnyilFU=';
$channelSecret = 'af771021d43101b42ce03499f1070b05';

$client = new LINEBotTiny($channelAccessToken, $channelSecret);
foreach ($client->parseEvents() as $event) {
    switch ($event['type']) {
        case 'message':
            $message = $event['message'];
            $ques = $message['text'];
            $ans = iconv('BIG-5', 'UTF-8', exec("python answer.py $ques"));
            
            switch ($message['type']) {
                case 'text':
                    $client->replyMessage([
                        'replyToken' => $event['replyToken'],
                        'messages' => [
                            [
                                'type' => 'text',
                                'text' => $ans
                            ]
                        ]
                    ]);
                    break;
                default:
                    error_log('Unsupported message type: ' . $message['type']);
                    break;
            }
            break;
        default:
            error_log('Unsupported event type: ' . $event['type']);
            break;
    }
};
