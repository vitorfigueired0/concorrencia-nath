# Resultado da coleta de dados 

Os dados foram coletados via postman, usando o collection runner.

## Sem Lock

| Requisções | Tempo médio | Tempo total | Sucessos | Falhas por falta de estoque | Erros da aplicação |
|------------|-------------|-------------|----------|-----------------------------|--------------------|
| 100        | 40ms        | 6s 356ms    | 100      | 0                           | 0                  |
| 1000       | 24ms        | 46s 784ms   | 327      | 673                         | 0                  |
| 10000      | 9ms         | 5m 7s       | 332      | 9668                        | 0                  |

Obs: durante a execução dos testes sem lock, não foi observado estoque abaixo de 0.

## Lock otimista

| Requisções | Tempo médio | Tempo total | Sucessos | Falhas por falta de estoque | Erros da aplicação |
|------------|-------------|-------------|----------|-----------------------------|--------------------|
| 100        | 45ms        | 7s 160ms    | 100      | 0                           | 0                  |
| 1000       | 25ms        | 47s 69ms    | 336      | 664                         | 0                  |
| 10000      | 9ms         | 4m 54s      | 320      | 9680                        | 0                  |

Obs: durante a execução dos testes com lock otimista, não foi observado estoque abaixo de 0.

## Lock pessimista 



