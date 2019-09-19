New BuildLocalizationHelper version 1.0.0.1 is attached:
Encoding control and non-english characters support
Dynamic separator
New syntax to make it to work:
BuildLocalizationHelper.exe -d "|" -k keysonly.txt -t translation.csv

Steps:
Export translations from excel with | delimiter. (for this I simply changed windows region - format additional settings - numbers list seperator (from ',' to '|') while doing a csv save as from excel