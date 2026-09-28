function get_random_number(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min) + min);
}

export function get_random_competencias(): string[]{
    var comps: string[] = ["java", "python", "javascript", "groovy", "typescript", "git", "junit", "spock", "postgresql", "regex"];
    //shuffle
    for (let i : number = 0; i < comps.length; i++){
        for (let j : number = 0; j < comps.length; j++){
            if (get_random_number(1, 4) == 2) {
                [comps[i], comps[j]] = [comps[j], comps[i]];
            }

        }
    }

    //pegar janela
    let i = 0 + get_random_number(0, comps.length/2);
    let j = comps.length-1 - get_random_number(0, comps.length/2);

    return comps.slice(i, j);
}