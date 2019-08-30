package com.app.vpn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton;

import de.blinkt.openvpn.BindUtils;
import de.blinkt.openvpn.BindStatus;
import de.blinkt.openvpn.base.StatusInfo;
import de.blinkt.openvpn.base.VPNActivity;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class MainActivity extends VPNActivity {

    String datasingapore = "client\n" +
            "cipher AES-256-CBC\n" +
            "setenv FORWARD_COMPATIBLE 1\n" +
            "server-poll-timeout 4\n" +
            "nobind\n" +
            "proto tcp\n" +
            "remote 52.213.137.169 443\n" +
            "dev tun\n" +
            "dev-type tun\n" +
            "ns-cert-type server\n" +
            "setenv opt tls-version-min 1.0 or-highest\n" +
            "reneg-sec 604800\n" +
            "key-direction 1\n" +
            "sndbuf 0\n" +
            "rcvbuf 0\n" +
            "comp-lzo no\n" +
            "verb 3\n" +
            "setenv PUSH_PEER_INFO\n" +
            "<ca>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIICuDCCAaCgAwIBAgIEXOtxzzANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDDApP\n" +
            "cGVuVlBOIENBMB4XDTE5MDUyMDA1MTI0N1oXDTI5MDUyNDA1MTI0N1owFTETMBEG\n" +
            "A1UEAwwKT3BlblZQTiBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "ALnKaUfJNedj0uioLcCSlChUoHW3FvbhCKBUknbIbujzM33Hm3UnoidNaxBB1qJ/\n" +
            "WTNhebhofcRZi5w1Cj5o8V2Y6Zlg+SOB05UX4H8AqNpczRn4R/5J/ln5/eqm+Il3\n" +
            "TPDlxsxz0u6a0nfn2ZxbfLU/QwKnxXU3LvCM1HDO0GAo4GJNengB4B9POB4+4vg7\n" +
            "5cl9xrOPgXb+YejU+/oR64vUwmxd6hY3DmP35F57oWb2zNJHZlLnWzaFJDbeIXoC\n" +
            "WJHjxuHbCU99n/dd+mWvLn02DAWQyrAtMCIDd2lkwnMSKvsOc0DLiL9WMD1uS/rL\n" +
            "t2opyTJTEJcpqTNs0Ftu/OMCAwEAAaMQMA4wDAYDVR0TBAUwAwEB/zANBgkqhkiG\n" +
            "9w0BAQsFAAOCAQEAIj47nsjVHD6SAoQ4+1W6W5LRq3PfK/AzuD2CukMmuYvenkSu\n" +
            "HVnMXwGEa1Du0GV+sryZ3QMUx41i48O/fPE46D4U8v/QsH33u1B84YEgfcK+P0O+\n" +
            "8NnKWdhSnGojVux9np12spTBzDhvn6gW/K0skNRkNQuA2wE1Obq8zBEe9bGbbokK\n" +
            "l5KGvmwp8SoG46aYTS1t5g2LU4A3k6i9or3e+fQEpjhkRXLTCza8d8x/wPdmAgeB\n" +
            "y+jGDpZRaMt8I07mhVEzELsF8q3LZacPVp/AJwp3fAaRh1gUkZBIC6OKTtplqL7U\n" +
            "5Njw+0dB3T8vnxzJm2K4KXewrmBybtVxT8RJCw==\n" +
            "-----END CERTIFICATE-----\n" +
            "</ca>\n" +
            "<cert>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIICzjCCAbagAwIBAgIBCjANBgkqhkiG9w0BAQsFADAVMRMwEQYDVQQDDApPcGVu\n" +
            "VlBOIENBMB4XDTE5MDgxNTA3MTkyNVoXDTI5MDgxOTA3MTkyNVowHjEcMBoGA1UE\n" +
            "AwwTc3RhbmlzbGF2X0FVVE9MT0dJTjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\n" +
            "AQoCggEBAMVt9A9V0BOkqNrmLos54zWCMEsCxDU6VTDIV0m7tSWWbRyQQJTrPbY9\n" +
            "v5NUkzrIabd4Z6XYziAWwZsfNLB+ANli6AxmxQ/7YCZKctQfC4AAosWSwHTDK2Os\n" +
            "nvusdLsMH0cWBQt5SgxvH89DV5JP9oEFORQ1pH3Dc5Ql+uvdUKf8oOETYiwF/CTv\n" +
            "Oy0RMJEy/Cl+OA99JOoyMwgs9TBLqPXGC6NsM5ha5sS7BB4syreGollYyoWB1vA0\n" +
            "J1jUIZyL9psiUewfMWCjGUlXphCjMLYDE7MVBDZKiiTzWact1LD8IpF/n6t7ONMl\n" +
            "KS99ceiHqRvDDY7hsgl46czRcJ0dN0MCAwEAAaMgMB4wCQYDVR0TBAIwADARBglg\n" +
            "hkgBhvhCAQEEBAMCB4AwDQYJKoZIhvcNAQELBQADggEBAJ8iSmjgxzHSivNO3FgX\n" +
            "tpSSqgK7ji0IgqooAmwl/MSExi5qQG+C21gJw5CeGTZp0rSeHQuzXhXTdRSygWBR\n" +
            "jLy3s/38pKAcEhw0tswhcbyYLk9PdnRNXKWKlUr7EX4T09zBBrlxR3bmU93Z10eP\n" +
            "fCV/P8LglR00iXiGjon9kewnh1EX1EDsZ8W0VIjVCXRqF2D+CMur9ye5uu2GmrRb\n" +
            "cvBL1OEQFf6KNf4gpbguVY3htpV7VS514VbhCrohK2AsIa6CCqWXbWJWnpaRlBHp\n" +
            "Tl+ndFvkl0X/c8N77zzGN7L57M5OA5NOf4nFgpjwWibgoNeFpDPRKui12JGrzP19\n" +
            "0rE=\n" +
            "-----END CERTIFICATE-----\n" +
            "</cert>\n" +
            "<key>\n" +
            "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDFbfQPVdATpKja\n" +
            "5i6LOeM1gjBLAsQ1OlUwyFdJu7Ullm0ckECU6z22Pb+TVJM6yGm3eGel2M4gFsGb\n" +
            "HzSwfgDZYugMZsUP+2AmSnLUHwuAAKLFksB0wytjrJ77rHS7DB9HFgULeUoMbx/P\n" +
            "Q1eST/aBBTkUNaR9w3OUJfrr3VCn/KDhE2IsBfwk7zstETCRMvwpfjgPfSTqMjMI\n" +
            "LPUwS6j1xgujbDOYWubEuwQeLMq3hqJZWMqFgdbwNCdY1CGci/abIlHsHzFgoxlJ\n" +
            "V6YQozC2AxOzFQQ2Sook81mnLdSw/CKRf5+rezjTJSkvfXHoh6kbww2O4bIJeOnM\n" +
            "0XCdHTdDAgMBAAECggEAGMijwaURGNYryEbrAtlCd34fV9tCDDiFZzlPE9d3dv96\n" +
            "NQK6yLb7KTx5JyKP1OpBsyFvSXd88LRKm5GASMOYkbGD1cK07DuvJz9jSNOY2vm5\n" +
            "dTnl5QJ3GO0bjBZrFOwBpSfTfEw+im8MvDUE8j4NgMbKEoplWtBFWwylgiRnMp8g\n" +
            "kAnu5LMtxsSXTmNxv8YgnGKVmSEfSvSe4syys25KwW+fVpKejmptmZp4hh06IVJg\n" +
            "viC4Fq75juqwjUzZ5/e00REc6Zvhgob80ZwNYX7bV2E6H2v14wgAyqfaDkHMfKw7\n" +
            "KWve4wqPFYBow9A1Z8/7H2a1bY9V1fsQFgjMAvimKQKBgQDm/cp8PFMyTx+nPO6R\n" +
            "a1ag0MxEWh6zx6Y7Sju0SGHilOk0h2CaioeQTHrzDFaG88fK+oxYF6MP2P6k3ij7\n" +
            "2A4UsiQ0tBb2i9WKa8h4sCAXFd5UdBfFXlc5QbXq643bpMonfaSaf1C9uKMCdSZh\n" +
            "L2U6B41v/0YUaRv/wcBh2eTpVQKBgQDazfRDDMpkNWQY5t7AR+AkIP1RaSwE6Exe\n" +
            "khyFk8XcKzSBCKEsbFwQzDOtmfNh9lN0Txnr9bmtu89+T09YhmzePtDO7+DNAaiE\n" +
            "m//qJamQ29boXNPxjI6oZkD9Jvu2wO6OJccobk6SoEvf9fYREJevWtmzdYIOanir\n" +
            "JxNb4Lq+NwKBgQDjtcDM5DMn3zRXCwwrsuL9OZiEsUqe8+v9etqMiOZmFWIn9GTZ\n" +
            "NEMbjymNO9Z4xw1oHcIIfZSz5LhzEsbYLk8DO/HROs+AUK07/bDsuMOJvnckTiW4\n" +
            "3Z2ixhJt+bQomLToZUXFQEj6HnAj1hT1rWFe6V8Wz1HogQ2m/PuH+uNc2QKBgQCT\n" +
            "YzxwlgMXcWeV5p6Rw1jWvWomFsRSIfCfm6ieyiXz4x6g9KEs/Wke347IF/X8PSOG\n" +
            "K2p4qbODtglVOjNmnFZldsbNPoSdU8qiWTsQDIKmU4/2QeF6yoWmJgsM+99/AYMw\n" +
            "OX5oW7WNy3CQ0mwInraupnITEsqiz1GtaMr/juf8DQKBgDgxrrJOpU5Kk9jsRlSo\n" +
            "UicmwK/+2Pjk8BggaXpD3OomRfF3n532UNiHapfxagm2jf//kdH2HNRmLy9Fi9us\n" +
            "RN+HmQUPNM/Hh5zBtgg3F/ztz8xJBW6t1N3qNf1svdJYOTX/DJK93SihEDebsNbq\n" +
            "mJH8/+9wIq61bBn+VqUkozly\n" +
            "-----END PRIVATE KEY-----\n" +
            "</key>\n" +
            "<tls-auth>\n" +
            "-----BEGIN OpenVPN Static key V1-----\n" +
            "e87abc1d313265117c8741483284f1dd\n" +
            "3caf8f0bcb2d4c7eacf2c9e1609031d3\n" +
            "10628d7fa70d21d8252543daf67e5807\n" +
            "9e90717b3488be7b5c9e28ee2de5010c\n" +
            "a2419fed0e317b061262871b7dbc615a\n" +
            "70c1466641a639fcdc2acbd48f9b562c\n" +
            "a9d6bd8a5fa836ae51541673be166b42\n" +
            "51a4452a8c92db8a7110c9f8a0271686\n" +
            "157623756cb5068fade8749d9509994c\n" +
            "8e6a99bd49b006c1649517bae0a6dbb6\n" +
            "e9b27e02745e7353bd9152caef42dd97\n" +
            "f6b96dbd96a01939ae8b7d8abfb87bb4\n" +
            "55edd988dc22a246f1ae2a45374d8ae0\n" +
            "cdc775f43bac735abae332c014d92fc3\n" +
            "c1632c186ce8436798b700a6b601c734\n" +
            "3bdb7ebbc2973e6f308d3a410e378ef7\n" +
            "-----END OpenVPN Static key V1-----\n" +
            "</tls-auth>\n";

    String datafrance = "client\n" +
            "dev tun\n" +
            "proto udp\n" +
            "remote vpn.net 1194\n" +
            "resolv-retry infinite\n" +
            "nobind\n" +
            "persist-key\n" +
            "persist-tun\n" +
            "auth SHA1\n" +
            "cipher AES-128-CBC\n" +
            "verb 3\n" +
            "auth-user-pass\n" +
            "<ca>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIID+jCCAuKgAwIBAgIBADANBgkqhkiG9w0BAQsFADB8MSMwIQYDVQQDDBp2cG44\n" +
            "NzUxNTYxMzIuc29mdGV0aGVyLm5ldDEjMCEGA1UECgwadnBuODc1MTU2MTMyLnNv\n" +
            "ZnRldGhlci5uZXQxIzAhBgNVBAsMGnZwbjg3NTE1NjEzMi5zb2Z0ZXRoZXIubmV0\n" +
            "MQswCQYDVQQGEwJVUzAeFw0xOTA1MjExMTQyNDJaFw0zNzEyMzExMTQyNDJaMHwx\n" +
            "IzAhBgNVBAMMGnZwbjg3NTE1NjEzMi5zb2Z0ZXRoZXIubmV0MSMwIQYDVQQKDBp2\n" +
            "cG44NzUxNTYxMzIuc29mdGV0aGVyLm5ldDEjMCEGA1UECwwadnBuODc1MTU2MTMy\n" +
            "LnNvZnRldGhlci5uZXQxCzAJBgNVBAYTAlVTMIIBIjANBgkqhkiG9w0BAQEFAAOC\n" +
            "AQ8AMIIBCgKCAQEA2a1P1mv3SryfZ6Re5MN1h1vBbMFmrX2gNP2KdEsHxBxiadrI\n" +
            "hw/yUz3vieN5CT3aZVFgqCzUkniKLEU/szfkYNb1ipQXHELaufEaltGHCStq6fCA\n" +
            "JbwxhMIO/RZGrdDTjLyq/zviaJrZk/+/yS4wMpzymXO+/mZ1x2UQDZi/Mpt1PNqp\n" +
            "i5dML1w2eNTltQImeXZmL9cmiTsd3gJlM68qB0rlFCwwYKRIsVbUiDc8oFubCTrd\n" +
            "6r8BZY2hHCY5/h7k+vqtx9xbpFHrRNME7No1Opjyo822mwovBf79a7UisOdXxCYD\n" +
            "3mJ25vpwBENuzVHxMrdbehb3HCdninzTaKn7bQIDAQABo4GGMIGDMA8GA1UdEwEB\n" +
            "/wQFMAMBAf8wCwYDVR0PBAQDAgH2MGMGA1UdJQRcMFoGCCsGAQUFBwMBBggrBgEF\n" +
            "BQcDAgYIKwYBBQUHAwMGCCsGAQUFBwMEBggrBgEFBQcDBQYIKwYBBQUHAwYGCCsG\n" +
            "AQUFBwMHBggrBgEFBQcDCAYIKwYBBQUHAwkwDQYJKoZIhvcNAQELBQADggEBACNa\n" +
            "F1SOVWn+i4KgAq81K95dRaQWFIh2B7TP3Bfgrwip0idkXHPHM0vklo4P0hNTyWvC\n" +
            "evnARH34lW5WfXnfdUyt29AEXurt29zEh0cNNkVsT4B1LDSgXdEGIqhQJLsVaGcE\n" +
            "QL71JJsIkW64er+pqPxH1nDmXsmo09ZcE02Vyh7hsXg0JyQ4Kw0rGLH6OaCRB08i\n" +
            "dIiweSeCceifXfmzLtef5mbZo4sSzbcFYwfCmxsSPETHVZV6LWbW1C+PByCWINW+\n" +
            "81Z+PkTDCDQJ74WGQ4eMHf6Bf7BA0oa/aBWfuFCeC9abTzLA6iJuXeOy1feEKzGo\n" +
            "jV7tkNacyrZKSgmAEhg=\n" +
            "-----END CERTIFICATE-----\n" +
            "</ca>\n" +
            "<cert>\n" +
            "-----BEGIN CERTIFICATE-----\n" +
            "MIID0DCCArigAwIBAgIBADANBgkqhkiG9w0BAQsFADBnMRwwGgYDVQQDDBM1MTk5\n" +
            "NTQzNzE1MjkwNjUwMDE3MRwwGgYDVQQKDBM1MTk5NTQzNzE1MjkwNjUwMDE3MRww\n" +
            "GgYDVQQLDBM1MTk5NTQzNzE1MjkwNjUwMDE3MQswCQYDVQQGEwJVUzAeFw0xOTA2\n" +
            "MDcxMTQ5NTlaFw0zNzEyMzExMTQ5NTlaMGcxHDAaBgNVBAMMEzUxOTk1NDM3MTUy\n" +
            "OTA2NTAwMTcxHDAaBgNVBAoMEzUxOTk1NDM3MTUyOTA2NTAwMTcxHDAaBgNVBAsM\n" +
            "EzUxOTk1NDM3MTUyOTA2NTAwMTcxCzAJBgNVBAYTAlVTMIIBIjANBgkqhkiG9w0B\n" +
            "AQEFAAOCAQ8AMIIBCgKCAQEA1KkIkv8yzyhTkQCO6F2V9I2djcaz4C/OOnxDG6Fd\n" +
            "gyk4whh8Q86o7bp33edSrjcEz68Qje3+orY0Bf68yRfrbyhIRCgjSFlaOex6SrAm\n" +
            "5v+qoTzYKGGIkD7ymjH5B29tU/KYgHEI0aA1IMCU8Pv3z5kUk9wOAsU+E8J3IXdJ\n" +
            "2szGuYvPyDYKkHTm2Q+hDiCcAzfTtJ6NmcNjeqIRPj9z5UTdpBftQrIitNBQXi5N\n" +
            "6ObLVX+yoxhq8kXGFhm4U6f2NTpRsW5hElhJar3yasnl+N6YC2/GwqMNEccb6ZsN\n" +
            "i/eYKEeKU27jM9jvoCEidiDL5uBxTxX3tbHegervxx6eWQIDAQABo4GGMIGDMA8G\n" +
            "A1UdEwEB/wQFMAMBAf8wCwYDVR0PBAQDAgH2MGMGA1UdJQRcMFoGCCsGAQUFBwMB\n" +
            "BggrBgEFBQcDAgYIKwYBBQUHAwMGCCsGAQUFBwMEBggrBgEFBQcDBQYIKwYBBQUH\n" +
            "AwYGCCsGAQUFBwMHBggrBgEFBQcDCAYIKwYBBQUHAwkwDQYJKoZIhvcNAQELBQAD\n" +
            "ggEBAMV0uSCf9MHS41/dI9s6zN4j6dZGrapfBqnDfVB6wUrr6Cnx21afbxdKFid4\n" +
            "QLaKmXC198n9KBk8vDmaSkE65DOfZ39azH/tO6UaTwfv+J5nhIN9xFtGGJalgA3C\n" +
            "XxDFxI4+XHC2BlMOq+2z21PNAHkR2snlDK7wxrwo16UWGpjy+EFRtjOuPdjJHvtv\n" +
            "a4ApS1/HeTh0BEY1HftG+xfE0Gzta1hmAzUnBsJmVEjV6pZHF4l4/9twrdJGvVFU\n" +
            "eKaNIkPInT9XKPJ+LSTQeEo0e4c+G/PZUdPghO7+nXl2IHZrAN1yJTUWOB4xxefU\n" +
            "WV4SqTOa7MY2EGVPWlAq9mgDQ84=\n" +
            "-----END CERTIFICATE-----\n" +
            "</cert>\n" +
            "<key>\n" +
            "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDUqQiS/zLPKFOR\n" +
            "AI7oXZX0jZ2NxrPgL846fEMboV2DKTjCGHxDzqjtunfd51KuNwTPrxCN7f6itjQF\n" +
            "/rzJF+tvKEhEKCNIWVo57HpKsCbm/6qhPNgoYYiQPvKaMfkHb21T8piAcQjRoDUg\n" +
            "wJTw+/fPmRST3A4CxT4Twnchd0nazMa5i8/INgqQdObZD6EOIJwDN9O0no2Zw2N6\n" +
            "ohE+P3PlRN2kF+1CsiK00FBeLk3o5stVf7KjGGryRcYWGbhTp/Y1OlGxbmESWElq\n" +
            "vfJqyeX43pgLb8bCow0Rxxvpmw2L95goR4pTbuMz2O+gISJ2IMvm4HFPFfe1sd6B\n" +
            "6u/HHp5ZAgMBAAECggEBALB7pzX2u7/Xg6JuiCqmn+/YJsyl/lkq00cOINMpU2Wq\n" +
            "LARINbhAJUlE2n05oBAccM26R07CF/QIqZvgD+QDCcux7mdBuCYuoQY5DjQ1g9No\n" +
            "kb5FtE2uw5qR4tu8ZSKagmyMg6r0I5F0FyMbL3/eW4XZUI50rNv3vNDBLgSD7rK4\n" +
            "gzMiRBeU4zSmPjcQMWUC6T+6r5GYDGrfGgWMxG2RhfmfJ3zeLlBjuIvq6yOTlz3o\n" +
            "xRcEbj5IeITiiDpQBsTmbzmGkDdm3IJwMFA2iCFDbmA0ecR0Zdslrzgd63iDaw8k\n" +
            "ch07fkR3QcsaWxm54p3QOScdx9N4Ec13IYXYKJwnPZECgYEA8D979qVS1gfq6s73\n" +
            "DkVC2jTxev/bymYPUkF88eBu5S2f4qEVCHULaQxauWicKFvCG8vk04OLurSWLi3b\n" +
            "AmcNoqgz0zMVJ5h6Qq8t0kjIrqLJ2EeLsouPOdXmVcNDxJJUrMeII4AYey6Ouxu9\n" +
            "ZzoPHcdF3RuW81CzmBG6nV9WRR8CgYEA4pp+vzOtXR5c5cdqc+ue5kzahg+H5c/0\n" +
            "4wRbno4nrOIiGqRk88JdKTuLMIGPiQLMJNDvlxg2e2xFIu1FUGaoM6gvb6Xo6vVf\n" +
            "pFZZzlip4FEAw30bg/9LDe4dRPFYpIWpA4HSAXnvv0afCEWOlOJSrZ5DPgVea6Nn\n" +
            "EyWkXTrXdYcCgYBIzqRtsdpxvIKH2GLssza4VDUeVm2wX8+i2O/ZiW9yCqqajsCG\n" +
            "CJqFolXYPIiefnhL3jWhmAr/cPzWT9VAXCS4t4rFJYTys/XRQa98O8x9iB3tank+\n" +
            "iabFGzqroPpnlwUMO4fZYleI9SoSMCztHJ46IwrcICFV6Y4TtsO8L4lAdwKBgQCc\n" +
            "D4jBB8cavYF/IoKXCSvrMfzBnUqbpEfkXSj4HrEGaMrSDGwWUMK2VfK7Wk9PLeUI\n" +
            "afjH134WLG/E6SH2iGzI/siEnP+3aXwQAwRgj0GXYlIjUDM2CglXs6jmxUymKgNH\n" +
            "jg3ixGawFeQK31SeBuyn87odqbbobKXdUuxK5O5kWwKBgQCUowq+tngMuICFr0Ea\n" +
            "DVyzFKNXbcKWV3qFmbR8ivJsh86s/W8dCWurLspOjXpMKG0cv2zxzS+BUE/lhKz6\n" +
            "B/zpEdShDPhQaeD6P5TFadE3y0OANpYESiiUDR3yhKEw2ChJJySoDLml7WuDKcZC\n" +
            "ha/7br3tAICKg0eBQ4oVL/Yvpg==\n" +
            "-----END PRIVATE KEY-----\n" +
            "</key>\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUtils.bind(this);

        // VPN SINGAPORE
        final Switch sButton = (Switch) findViewById(R.id.singapore);
        sButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                loadVpnProfile(datasingapore);
                getVpnProfile().mName = "VPN 1";
                connectVpn();
                Toast.makeText(getApplicationContext(), "VPN 1" , Toast.LENGTH_LONG).show();
                sButton.setChecked(true);
            }
            else
            {
                stopVpn();
            }
            }
        });

        // VPN FRANCE
        final Switch sButton2 = (Switch) findViewById(R.id.france);
        sButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    loadVpnProfile(datafrance);
                    getVpnProfile().mName = "VPN 2";
                    connectVpn();
                    Toast.makeText(getApplicationContext(), "VPN 2" , Toast.LENGTH_LONG).show();
                    sButton.setChecked(false);
                }
                else
                {
                    stopVpn();
                }
            }
        });
    }

    @BindStatus
    public void onStatus(StatusInfo statusInfo) {
        String TAG = "TAG";
        Log.d(TAG, "onStatus: " + statusInfo.toString());
        switch (statusInfo.getLevel()) {
            case LEVEL_START:
                break;
            case LEVEL_CONNECTED:
                break;
            case LEVEL_VPNPAUSED:
                break;
            case LEVEL_NONETWORK:
                break;
            case LEVEL_CONNECTING_SERVER_REPLIED:
                break;
            case LEVEL_CONNECTING_NO_SERVER_REPLY_YET:
                break;
            case LEVEL_NOTCONNECTED:
                break;
            case LEVEL_AUTH_FAILED:
                break;
            case LEVEL_WAITING_FOR_USER_INPUT:
                break;
            case UNKNOWN_LEVEL:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        BindUtils.unBind(this);
        super.onDestroy();
    }

    @Override
    public Intent getJumpIntent() {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), MainActivity.class.getName());
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
}