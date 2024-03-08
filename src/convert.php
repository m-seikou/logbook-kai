<?php
const convertKeys = ['frai', 'erai', 'fydam', 'eydam', 'fcl', 'ecl'];

/**
 * @param string $path
 * @return string[]
 */
function fetchDir(string $path): array
{
    $dir = opendir($path);
    if ($dir === false) {
        return [];
    }
    $result = [];
    while (($file = readdir($dir)) !== false) {
        if (!str_ends_with($file, ".json")) {
            continue;
        }
        $result[] = $path . DIRECTORY_SEPARATOR . $file;
    }
    return $result;
}

function convertFile(string $input, string $output): void
{
    try {
        $data = json_decode(file_get_contents($input), true, 512, JSON_THROW_ON_ERROR);
    } catch (JsonException $e) {
        var_dump($e);
        return;
    }
    if (isset($data["battle"]["openingFlag"]) && $data["battle"]["openingFlag"]) {
        //変換
        $data["battle"]["openingAtack"] = convertOpeningAttack($data["battle"]["openingAtack"]);
    } elseif ($input === $output) {
        // 変更の必要が無いので正常終了とする
        return;
    }

    try {
        file_put_contents($output, json_encode($data, JSON_THROW_ON_ERROR));
    } catch (JsonException $e) {
        var_dump($e);
        return;
    }
}

function convertOpeningAttack(array $openingAttack): array
{
    foreach (convertKeys as $convertKey) {
        $out = [];
        if(!is_iterable($openingAttack[$convertKey])){
            continue;
        }
        foreach ($openingAttack[$convertKey] as $target) {
            $out[] = is_scalar($target) ? [$target] : $target;
        }
        $openingAttack[$convertKey] = $out;
    }
    return $openingAttack;
}

$dir = $argv[1];
foreach (fetchDir($dir) as $file) {
    echo $file . "\n";
    convertFile($file, $file);
}